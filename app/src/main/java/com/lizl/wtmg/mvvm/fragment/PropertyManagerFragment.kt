package com.lizl.wtmg.mvvm.fragment

import androidx.lifecycle.Observer
import com.blankj.utilcode.util.ActivityUtils
import com.lizl.wtmg.R
import com.lizl.wtmg.databinding.FragmentPropertyManagerBinding
import com.lizl.wtmg.db.AppDatabase
import com.lizl.wtmg.mvvm.activity.AddPropertyActivity
import com.lizl.wtmg.mvvm.adapter.PropertyCategoryGroupAdapter
import com.lizl.wtmg.mvvm.base.BaseFragment
import com.lizl.wtmg.mvvm.model.PropertyCategoryGroupModel
import kotlinx.android.synthetic.main.fragment_property_manager.*

class PropertyManagerFragment : BaseFragment<FragmentPropertyManagerBinding>(R.layout.fragment_property_manager)
{
    private lateinit var propertyCategoryGroupAdapter: PropertyCategoryGroupAdapter

    override fun initView()
    {
        propertyCategoryGroupAdapter = PropertyCategoryGroupAdapter()
        rv_property_category_group.adapter = propertyCategoryGroupAdapter
    }

    override fun initData()
    {
        AppDatabase.getInstance().getPropertyDao().obAllProperty().observe(this, Observer { propertyList ->
            tv_total_property.text = propertyList.sumBy { it.amount }.toString()
            tv_net_property.text = propertyList.sumBy { it.amount }.toString()
            tv_total_liabilities.text = 0.toString()

            val categoryGroupList = mutableListOf<PropertyCategoryGroupModel>()
            propertyList.groupBy { it.category }.forEach { (t, u) ->
                categoryGroupList.add(PropertyCategoryGroupModel(t, u.toMutableList()))
            }
            propertyCategoryGroupAdapter.replaceData(categoryGroupList)
        })
    }

    override fun initListener()
    {
        fab_add.setOnClickListener { ActivityUtils.startActivity(AddPropertyActivity::class.java) }
    }
}