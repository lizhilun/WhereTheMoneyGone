package com.lizl.wtmg.mvvm.activity

import com.blankj.utilcode.util.ActivityUtils
import com.lizl.wtmg.R
import com.lizl.wtmg.databinding.ActivityPropertyManagerBinding
import com.lizl.wtmg.db.AppDatabase
import com.lizl.wtmg.mvvm.base.BaseActivity
import kotlinx.android.synthetic.main.activity_property_manager.*

class PropertyManagerActivity : BaseActivity<ActivityPropertyManagerBinding>(R.layout.activity_property_manager)
{
    override fun initData()
    {
        AppDatabase.getInstance().getPropertyDao().getAllPropertyLiveData().observe(this, { propertyList ->
            tv_total_property.text = propertyList.sumBy { it.amount }.toString()
        })
    }

    override fun initListener()
    {
        fab_add.setOnClickListener { ActivityUtils.startActivity(AddPropertyActivity::class.java) }
    }
}