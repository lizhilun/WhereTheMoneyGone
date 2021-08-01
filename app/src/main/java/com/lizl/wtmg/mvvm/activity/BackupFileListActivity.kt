package com.lizl.wtmg.mvvm.activity

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.ToastUtils
import com.lizl.wtmg.R
import com.lizl.wtmg.constant.AppConstant
import com.lizl.wtmg.custom.function.getFileName
import com.lizl.wtmg.custom.function.setOnItemClickListener
import com.lizl.wtmg.custom.function.ui
import com.lizl.wtmg.custom.popup.PopupUtil
import com.lizl.wtmg.custom.popup.operaion.OperationModel
import com.lizl.wtmg.custom.view.titlebar.TitleBarBtnBean
import com.lizl.wtmg.databinding.ActivityBackupFileListBinding
import com.lizl.wtmg.module.backup.BackupUtil
import com.lizl.wtmg.module.backup.FileUtil
import com.lizl.wtmg.module.config.constant.ConfigConstant
import com.lizl.wtmg.module.config.util.ConfigUtil
import com.lizl.wtmg.mvvm.adapter.BackupFileListAdapter
import com.lizl.wtmg.mvvm.base.BaseActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class BackupFileListActivity : BaseActivity<ActivityBackupFileListBinding>(R.layout.activity_backup_file_list)
{
    private val backupFileListAdapter = BackupFileListAdapter()

    companion object
    {
        const val REQUEST_CODE_SELECT_BACKUP_FILE = 322
    }

    override fun initView()
    {
        dataBinding.ctbTitle.setActionList(mutableListOf<TitleBarBtnBean.BaseBtnBean>().apply {
            add(TitleBarBtnBean.ImageBtnBean(R.drawable.ic_baseline_folder_open_24) {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "*/*"
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                startActivityForResult(intent, REQUEST_CODE_SELECT_BACKUP_FILE)
            })
        })

        dataBinding.rvBackupFile.adapter = backupFileListAdapter
    }

    override fun initListener()
    {
        dataBinding.ctbTitle.setOnBackBtnClickListener { onBackPressed() }

        backupFileListAdapter.setOnItemClickListener { backupFile ->

            val operationList = mutableListOf<OperationModel>().apply {

                // 覆盖导入备份
                add(OperationModel(getString(R.string.import_backup_file_overlay)) {
                    PopupUtil.showConfirmPopup(getString(R.string.notify_restore_data_overlay)) {
                        restoreData(Uri.fromFile(backupFile), true)
                    }
                })

                // 合并导入备份
                add(OperationModel(getString(R.string.import_backup_file_merge)) {
                    PopupUtil.showConfirmPopup(getString(R.string.notify_restore_data_merge)) {
                        restoreData(Uri.fromFile(backupFile), false)
                    }
                })

                // 删除备份文件
                add(OperationModel(getString(R.string.delete_backup_file)) {
                    PopupUtil.showConfirmPopup(getString(R.string.notify_delete_backup_file)) {
                        if (FileUtil.deleteFile(backupFile.absolutePath))
                        {
                            backupFileListAdapter.remove(backupFile)
                        }
                    }
                })

                // 分享备份文件
                add(OperationModel(getString(R.string.share_backup_file)) {
                    PopupUtil.showConfirmPopup(getString(R.string.notify_share_backup_file)) {
                        FileUtil.shareAllTypeFile(backupFile)
                    }
                })
            }

            PopupUtil.showOperationPopup(operationList)
        }
    }

    override fun initData()
    {
        lifecycleScope.launch {
            val backupFileList = BackupUtil.getBackupFileList()
            ui { backupFileListAdapter.setNewData(backupFileList.toMutableList()) }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG, "onActivityResult() called with: requestCode = [$requestCode], resultCode = [$resultCode], data = [$data]")
        when (requestCode)
        {
            REQUEST_CODE_SELECT_BACKUP_FILE ->
            {
                if (resultCode != RESULT_OK) return
                val fileUri = data?.data ?: return

                if (fileUri.getFileName(this)?.endsWith(BackupUtil.fileSuffixName) != true)
                {
                    ToastUtils.showShort(R.string.notify_wrong_backup_file)
                    return
                }

                val operationList = mutableListOf<OperationModel>().apply {

                    // 覆盖导入备份
                    add(OperationModel(getString(R.string.import_backup_file_overlay)) {
                        restoreData(fileUri, true)
                    })

                    // 合并导入备份
                    add(OperationModel(getString(R.string.import_backup_file_merge)) {
                        restoreData(fileUri, false)
                    })
                }

                PopupUtil.showOperationPopup(operationList)
            }
        }
    }

    private fun restoreData(fileUri: Uri, clearOriData: Boolean)
    {
        restoreData(fileUri, ConfigUtil.getStringBlocking(ConfigConstant.CONFIG_APP_LOCK_PASSWORD), clearOriData)
    }

    private fun restoreData(fileUri: Uri, password: String, clearOriData: Boolean)
    {
        BackupUtil.restoreData(fileUri, password, clearOriData) { result: Boolean, failedReason: String ->
            if (result)
            {
                ToastUtils.showShort(R.string.notify_success_to_restore)
                onBackPressed()
            }
            else
            {
                if (failedReason == AppConstant.DATA_RESTORE_FAILED_WRONG_PASSWORD)
                {
                    PopupUtil.showInputPopup(getString(R.string.input_encrypt_password)) {
                        restoreData(fileUri, it, clearOriData)
                    }
                }
            }
        }
    }
}