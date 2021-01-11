package com.lizl.wtmg.mvvm.activity

import com.blankj.utilcode.util.ActivityUtils
import com.lizl.wtmg.R
import com.lizl.wtmg.databinding.ActivityMainBinding
import com.lizl.wtmg.mvvm.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main)
{
    override fun initListener()
    {
        fab_add.setOnClickListener { ActivityUtils.startActivity(MoneyRecordActivity::class.java) }
    }
}