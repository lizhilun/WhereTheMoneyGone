package com.lizl.wtmg.module.mainpic

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import com.blankj.utilcode.util.*
import com.jeremyliao.liveeventbus.LiveEventBus
import com.lizl.wtmg.R
import com.lizl.wtmg.constant.EventConstant
import com.lizl.wtmg.custom.function.io
import com.lizl.wtmg.custom.function.ui
import com.lizl.wtmg.module.skin.util.SkinUtil
import com.yalantis.ucrop.UCrop
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

object MainPicHandler
{
    private const val TAG = "MainPicHandler"

    private val mainImagePath = PathUtils.getExternalAppFilesPath() + "/image/cover_image.jpg"

    private val selectImageTempPath = PathUtils.getExternalAppFilesPath() + "/image/select_image_temp.jpg"
    private val cropImageTemp = PathUtils.getExternalAppFilesPath() + "/image/crop_image_temp.jpg"

    fun getMainImageBitmap(): Bitmap?
    {
        return if (FileUtils.isFileExists(mainImagePath))
        {
            ImageUtils.getBitmap(mainImagePath)
        }
        else null
    }

    fun onImageSelectFinish(activity: Activity, imageUri: Uri)
    {
        io {
            val bitmap = try
            {
                BitmapFactory.decodeStream(activity.contentResolver.openInputStream(imageUri))
            }
            catch (e: Exception)
            {
                Log.e(TAG, "decodeStream error:", e)
                null
            }

            if (bitmap == null)
            {
                ToastUtils.showShort(R.string.image_error_please_reselect)
                return@io
            }

            //TODO:先将图片保存到应用目录，再进行裁切
            try
            {
                ImageUtils.save(bitmap, selectImageTempPath, CompressFormat.JPEG)
            }
            catch (e: Exception)
            {
                Log.e(TAG, "saveImage error:", e)
                ToastUtils.showShort(R.string.image_error_please_reselect)
                return@io
            }

            ui {
                val options = UCrop.Options().apply {
                    setHideBottomControls(true)
                    setToolbarColor(SkinUtil.getColor(activity, R.color.colorWindowBg))
                    setStatusBarColor(SkinUtil.getColor(activity, R.color.colorWindowBg))
                    setFreeStyleCropEnabled(false)
                }
                UCrop.of(Uri.fromFile(File(selectImageTempPath)), Uri.fromFile(File(cropImageTemp))).withAspectRatio(16F, 10F).withOptions(options)
                    .start(activity)
            }
        }
    }

    fun onImageCropFinish(activity: Activity, imageUri: Uri)
    {
        io {
            try
            {
                val bitmap = BitmapFactory.decodeStream(activity.contentResolver.openInputStream(imageUri))
                ImageUtils.save(bitmap, mainImagePath, CompressFormat.JPEG)

                FileUtils.delete(selectImageTempPath)
                FileUtils.delete(cropImageTemp)
            }
            catch (e: Exception)
            {
                Log.e(TAG, "onImageCropFinish error:", e)
                ToastUtils.showShort(R.string.image_crop_error_please_reselect)
                return@io
            }

            LiveEventBus.get(EventConstant.EVENT_COVER_IMAGE_UPDATE).post(true)
        }
    }
}