package com.lizl.wtmg.mvvm.activity

import androidx.fragment.app.Fragment
import com.jeremyliao.liveeventbus.LiveEventBus
import com.lizl.wtmg.mvvm.adapter.FragmentPagerAdapter
import com.lizl.wtmg.R
import com.lizl.wtmg.constant.EventConstant
import com.lizl.wtmg.databinding.ActivityMainBinding
import com.lizl.wtmg.mvvm.base.BaseActivity
import com.lizl.wtmg.mvvm.fragment.PropertyManagerFragment
import com.lizl.wtmg.mvvm.fragment.PropertyOutlineFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main)
{
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
        LiveEventBus.get(EventConstant.EVENT_GO_TO_PROPERTY_MANAGER_VIEW).observe(this, {
            vp_content.setCurrentItem(1, true)
        })
    }
}