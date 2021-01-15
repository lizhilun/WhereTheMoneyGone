package com.lizl.wtmg.mvvm.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class FragmentPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity)
{
    private val fragmentList = mutableListOf<Fragment>()
    private val fragmentIds = mutableListOf<Long>()

    fun setFragmentList(fragmentList: List<Fragment>)
    {
        this.fragmentList.clear()
        this.fragmentList.addAll(fragmentList)
        this.fragmentIds.clear()
        this.fragmentList.forEach { fragmentIds.add(it.hashCode().toLong()) }
        notifyDataSetChanged()
    }

    override fun getItemCount() = fragmentList.size

    override fun createFragment(position: Int) = fragmentList[position]

    override fun getItemId(position: Int) = fragmentIds[position]

    override fun containsItem(itemId: Long) = fragmentIds.contains(itemId)
}