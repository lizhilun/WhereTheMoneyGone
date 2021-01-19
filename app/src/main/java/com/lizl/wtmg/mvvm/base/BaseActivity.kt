package com.lizl.wtmg.mvvm.base

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager.LayoutParams
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.SkinAppCompatDelegateImpl
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.lizl.wtmg.module.skin.util.SkinUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

open class BaseActivity<DB : ViewDataBinding>(private val layoutId: Int) : AppCompatActivity()
{
    protected val TAG = this.javaClass.simpleName

    protected lateinit var dataBinding: DB

    override fun onCreate(savedInstanceState: Bundle?)
    {
        Log.d(TAG, "onCreate")
        super.onCreate(savedInstanceState)

        dataBinding = DataBindingUtil.setContentView(this, layoutId)
        dataBinding.lifecycleOwner = this

        window.clearFlags(LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.addFlags(LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        initView()
        initData()
        initListener()
    }

    override fun onResume()
    {
        Log.d(TAG, "onResume")
        super.onResume()
    }

    override fun onStart()
    {
        Log.d(TAG, "onStart")
        super.onStart()
    }

    override fun onRestart()
    {
        Log.d(TAG, "onRestart")
        super.onRestart()
    }

    override fun onPause()
    {
        Log.d(TAG, "onPause")
        super.onPause()
    }

    override fun onStop()
    {
        Log.d(TAG, "onStop")
        super.onStop()
    }

    override fun onDestroy()
    {
        Log.d(TAG, "onDestroy")
        super.onDestroy()
    }

    override fun getDelegate(): AppCompatDelegate
    {
        return SkinAppCompatDelegateImpl.get(this, this)
    }

    override fun onConfigurationChanged(newConfig: Configuration)
    {
        super.onConfigurationChanged(newConfig)
        GlobalScope.launch { SkinUtil.loadSkin() }
    }

    open fun initView()
    {

    }

    open fun initData()
    {

    }

    open fun initListener()
    {

    }
}