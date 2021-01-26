package com.lizl.wtmg.module.backup

import android.net.Uri
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.PathUtils
import com.blankj.utilcode.util.TimeUtils
import com.lizl.wtmg.constant.AppConstant
import com.lizl.wtmg.db.AppDatabase
import com.lizl.wtmg.db.model.AccountModel
import com.lizl.wtmg.db.model.MoneyTracesModel
import com.lizl.wtmg.module.config.constant.ConfigConstant
import com.lizl.wtmg.module.config.util.ConfigUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

/**
 * 备份工具类
 */
object BackupUtil
{
    private val backupFilePath = PathUtils.getExternalAppFilesPath() + "/Backup"
    const val fileSuffixName = ".0516"
    private const val autoBackupFileName = "wmmgAutoBackup"

    private val backupChannel = Channel<BackupJob>()

    private var isFirstGetAccount = true
    private var isFirstGetTraces = true

    fun init()
    {
        GlobalScope.launch {
            for (job in backupChannel)
            {
                backupData(job)
            }
        }

        //启动数据监听，用于自动备份
        GlobalScope.launch(Dispatchers.Main) {
            AppDatabase.getInstance().getAccountDao().obAllAccountForBackup().observeForever {
                if (isFirstGetAccount)
                {
                    isFirstGetAccount = false
                    return@observeForever
                }
                autoBackupData()
            }

            AppDatabase.getInstance().getMoneyTracesDao().obAllTracesForBackup().observeForever {
                if (isFirstGetTraces)
                {
                    isFirstGetTraces = false
                    return@observeForever
                }
                autoBackupData()
            }
        }
    }

    /**
     * 自动备份
     */
    private fun autoBackupData()
    {
        GlobalScope.launch { backupChannel.send(BackupJob(autoBackupFileName) {}) }
    }

    /**
     * 备份数据
     */
    fun backupData(callback: (result: Boolean) -> Unit)
    {
        GlobalScope.launch {
            val backupFileName = TimeUtils.millis2String(System.currentTimeMillis(), "yyyyMMdd_HHmmss")
            backupChannel.send(BackupJob(backupFileName, callback))
        }
    }

    /**
     * 备份数据
     */
    private suspend fun backupData(backupJob: BackupJob)
    {
        val backupFileName = "${backupJob.backupFileName}$fileSuffixName"
        val accountList = AppDatabase.getInstance().getAccountDao().queryAllAccount()
        val tracesList = AppDatabase.getInstance().getMoneyTracesDao().queryAllTraces()
        val backupDataModel = BackupDataModel(accountList, tracesList)

        val dataString = GsonUtils.toJson(backupDataModel)
        val encryptData = EncryptUtil.encrypt(dataString, ConfigUtil.getString(ConfigConstant.CONFIG_APP_LOCK_PASSWORD))
        FileUtil.writeTxtFile(encryptData, "$backupFilePath/$backupFileName")

        FileUtils.notifySystemToScan(backupFilePath)

        delay(200)

        GlobalScope.launch(Dispatchers.Main) { backupJob.callback.invoke(true) }
    }

    /**
     * 还原数据
     */
    fun restoreData(fileUri: Uri, password: String, clearAllData: Boolean, callback: (result: Boolean, failedReason: String) -> Unit)
    {
        GlobalScope.launch(Dispatchers.IO) {

            val readResult = EncryptUtil.decrypt(FileUtil.readTxtFile(fileUri), password)

            if (readResult == null)
            {
                GlobalScope.launch(Dispatchers.Main) { callback.invoke(false, AppConstant.DATA_RESTORE_FAILED_WRONG_PASSWORD) }
                return@launch
            }

            // 清空之前的数据
            if (clearAllData)
            {
                AppDatabase.getInstance().getAccountDao().deleteAll()
                AppDatabase.getInstance().getMoneyTracesDao().deleteAll()
            }

            val backupDataModel = GsonUtils.fromJson(readResult, BackupDataModel::class.java)
            backupDataModel.accountList.forEach { it.id = 0 }
            backupDataModel.tracesList.forEach { it.id = 0 }
            AppDatabase.getInstance().getAccountDao().insertList(backupDataModel.accountList)
            AppDatabase.getInstance().getMoneyTracesDao().insertList(backupDataModel.tracesList)

            GlobalScope.launch(Dispatchers.Main) { callback.invoke(true, "") }
        }
    }

    /**
     * 获取备份文件列表
     *
     * @return 文件路径列表
     */
    fun getBackupFileList(): List<File>
    {
        return File(backupFilePath).listFiles()?.filter { it.exists() && it.isFile && it.name.endsWith(fileSuffixName) } ?: emptyList()
    }

    private class BackupJob(val backupFileName: String, val callback: (result: Boolean) -> Unit)

    private class BackupDataModel(val accountList: MutableList<AccountModel>, val tracesList: MutableList<MoneyTracesModel>)
}