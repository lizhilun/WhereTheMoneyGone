package com.lizl.wtmg.mvvm.base

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.SkinAppCompatDelegateImpl
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.blankj.utilcode.util.ActivityUtils
import com.lizl.wtmg.custom.function.launchDefault
import com.lizl.wtmg.custom.other.CustomActivityLifecycle
import com.lizl.wtmg.module.skin.util.SkinUtil
import com.lizl.wtmg.mvvm.activity.LockActivity
import com.lizl.wtmg.util.ActivityUtil

open class BaseActivity<DB : ViewDataBinding>(private val layoutId: Int) : AppCompatActivity() {
    protected val TAG = this.javaClass.simpleName

    protected lateinit var dataBinding: DB

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate")
        super.onCreate(savedInstanceState)

        dataBinding = DataBindingUtil.setContentView(this, layoutId)
        dataBinding.lifecycleOwner = this

        initView()
        initData()
        initListener()
    }

    protected fun <T : ViewModel> createViewModel(clazz: Class<T>): T {
        return ViewModelProvider(this).get(clazz)
    }

    override fun onResume() {
        Log.d(TAG, "onResume")
        super.onResume()
    }

    override fun onStart() {
        Log.d(TAG, "onStart")
        super.onStart()
    }

    override fun onRestart() {
        Log.d(TAG, "onRestart")
        super.onRestart()
    }

    override fun onPause() {
        Log.d(TAG, "onPause")
        super.onPause()
    }

    override fun onStop() {
        Log.d(TAG, "onStop")
        super.onStop()
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
        super.onDestroy()
    }

    override fun onBackPressed() {
        if (ActivityUtil.getLastActivity() is LockActivity) {
            ActivityUtils.startHomeActivity()
            return
        }
        super.onBackPressed()
    }

    override fun getDelegate(): AppCompatDelegate {
        return SkinAppCompatDelegateImpl.get(this, this)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        launchDefault { SkinUtil.loadSkin() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        CustomActivityLifecycle.isFromActivityResult = true
        super.onActivityResult(requestCode, resultCode, data)
    }

    open fun initView() {

    }

    open fun initData() {

    }

    open fun initListener() {

    }
}