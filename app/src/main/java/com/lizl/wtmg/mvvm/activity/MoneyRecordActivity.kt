package com.lizl.wtmg.mvvm.activity

import com.lizl.wtmg.R
import com.lizl.wtmg.databinding.ActivityMoneyRecordBinding
import com.lizl.wtmg.mvvm.base.BaseActivity
import kotlinx.android.synthetic.main.activity_money_record.*

class MoneyRecordActivity : BaseActivity<ActivityMoneyRecordBinding>(R.layout.activity_money_record)
{
    override fun initListener()
    {
        ctb_title.setOnClickListener { onBackPressed() }
    }
}