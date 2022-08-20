package com.lizl.wtmg.module.backup

import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.FileIOUtils
import com.blankj.utilcode.util.FileUtils
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.util.*


/**
 * 文件工具类
 */
object FileUtil {
    private const val sBufferSize = 524288
    private const val TAG = "FileUtil"

    /**
     * 写文件
     *
     * @param content 文件内容
     * @param filePath 文件绝对路径
     */
    fun writeTxtFile(content: String, filePath: String) = FileIOUtils.writeFileFromString(filePath, content)

    /**
     * 读TXT文件内容
     * @param filePath 文件绝对路径
     */
    @Throws(Exception::class)
    fun readTxtFile(filePath: String) = FileIOUtils.readFile2String(filePath)

    /**
     * 读TXT文件内容
     * @par
     */
    fun readTxtFile(fileUrl: Uri): String {
        if (fileUrl.scheme == ContentResolver.SCHEME_CONTENT) {
            try {
                var os: ByteArrayOutputStream? = null
                val context = ActivityUtils.getTopActivity() ?: return ""
                val inputStream = context.contentResolver.openInputStream(fileUrl)?.buffered()
                        ?: return ""
                try {
                    os = ByteArrayOutputStream()
                    val b = ByteArray(sBufferSize)
                    var len: Int
                    while (inputStream.read(b, 0, sBufferSize).also { len = it } != -1) {
                        os.write(b, 0, len)
                    }
                    return String(os.toByteArray())
                } catch (e: IOException) {
                    e.printStackTrace()
                } finally {
                    try {
                        inputStream.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    try {
                        os?.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
            return ""
        } else if (fileUrl.scheme == ContentResolver.SCHEME_CONTENT || fileUrl.scheme == ContentResolver.SCHEME_FILE) {
            val filePath = fileUrl.path ?: return ""
            return readTxtFile(filePath)
        }
        return ""
    }

    /**
     * 删除文件
     */
    fun deleteFile(filePath: String) = FileUtils.deleteFile(filePath)

    /**
     * 重命名文件
     */
    fun renameFile(filePath: String, newName: String) = FileUtils.rename(filePath, newName)

    /**
     * 获取文件uri
     */
    private fun getFileUri(file: File): Uri {
        return FileProvider.getUriForFile(ActivityUtils.getTopActivity(), "com.lizl.wtmg.fileprovider", file);
    }

    /**
     * 获取文件大小
     */
    fun getFileSize(file: File): String {
        val len = FileUtils.getFileLength(file)
        return when {
            len < 0 -> "0B"
            len < 1024 -> "%.1fB".format(len.toDouble())
            len < 1024 * 1024 -> "%.1fKB".format(len.toDouble() / 1024)
            len < 1024 * 1024 * 1024 -> "%.1fMB".format(len.toDouble() / (1024 * 1024))
            else -> "%.1fGB".format(len.toDouble() / (1024 * 1024 * 1024))
        }
    }

    /**
     * 分享所有类型的文件
     */
    fun shareAllTypeFile(file: File) {
        try {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.putExtra(Intent.EXTRA_STREAM, getFileUri(file))
            shareIntent.type = "*/*"; //此处可发送多种文件
            ActivityUtils.getTopActivity().startActivity(Intent.createChooser(shareIntent, ""))
        } catch (e: Exception) {
            Log.e(TAG, "shareAllTypeFile error:", e)
        }
    }
}