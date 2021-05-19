package com.lizl.wtmg.mvvm.activity

import android.content.Intent
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.*
import com.jeremyliao.liveeventbus.LiveEventBus
import com.lizl.wtmg.R
import com.lizl.wtmg.constant.EventConstant
import com.lizl.wtmg.databinding.ActivityMainBinding
import com.lizl.wtmg.module.mainpic.MainPicHandler
import com.lizl.wtmg.mvvm.adapter.FragmentPagerAdapter
import com.lizl.wtmg.mvvm.base.BaseActivity
import com.lizl.wtmg.mvvm.fragment.PropertyManagerFragment
import com.lizl.wtmg.mvvm.fragment.PropertyOutlineFragment
import com.yalantis.ucrop.UCrop
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main)
{
    companion object
    {
        private const val REQUEST_CODE_SELECT_IMAGE_FILE = 516
    }

    override fun initView()
    {
        vp_content.offscreenPageLimit = 2

        vp_content.adapter = FragmentPagerAdapter(this).apply {
            setFragmentList(mutableListOf<Fragment>().apply {
                add(PropertyOutlineFragment())
                add(PropertyManagerFragment())
            })
        }
    }

    override fun initData()
    {
        LiveEventBus.get(EventConstant.EVENT_GO_TO_PROPERTY_MANAGER_VIEW).observe(this, Observer {
            vp_content.setCurrentItem(1, true)
        })

        LiveEventBus.get(EventConstant.EVENT_GO_TO_COVER_IMAGE_SELECTION).observe(this, Observer {
            selectImageFile()
        })
    }

    private fun selectImageFile()
    {
        val intent = Intent(Intent.ACTION_GET_CONTENT)

        intent.type = "image/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)

        startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE_FILE)
    }

    override fun onBackPressed()
    {
        if (vp_content.currentItem == 1)
        {
            vp_content.setCurrentItem(0, true)
            return
        }
        super.onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)

        Log.d(TAG, "onActivityResult() called with: requestCode = [$requestCode], resultCode = [$resultCode], data = [$data]")

        data ?: return

        when (requestCode)
        {
            REQUEST_CODE_SELECT_IMAGE_FILE ->
            {
                val imageUri = data.data ?: return
                MainPicHandler.onImageSelectFinish(this, imageUri)
            }
            UCrop.REQUEST_CROP             ->
            {
                if (resultCode == RESULT_OK)
                {
                    val imageUri = UCrop.getOutput(data) ?: return
                    MainPicHandler.onImageCropFinish(this, imageUri)
                }
                else if (resultCode == UCrop.RESULT_ERROR)
                {
                    val error = UCrop.getError(data)
                    Log.e(TAG, "cropImage error:", error)
                    ToastUtils.showShort(R.string.image_crop_error_please_reselect)
                }
            }
        }
    }
}