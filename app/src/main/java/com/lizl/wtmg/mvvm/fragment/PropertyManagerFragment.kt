package com.lizl.wtmg.mvvm.fragment

import androidx.lifecycle.Observer
import com.blankj.utilcode.util.ActivityUtils
import com.lizl.wtmg.R
import com.lizl.wtmg.R.dimen
import com.lizl.wtmg.custom.function.setOnClickListener
import com.lizl.wtmg.custom.view.ListDividerItemDecoration
import com.lizl.wtmg.databinding.FragmentPropertyManagerBinding
import com.lizl.wtmg.db.AppDatabase
import com.lizl.wtmg.module.property.PropertyManager
import com.lizl.wtmg.mvvm.activity.AddPropertyActivity
import com.lizl.wtmg.mvvm.adapter.PolymerizeGroupAdapter
import com.lizl.wtmg.mvvm.base.BaseFragment
import com.lizl.wtmg.mvvm.model.polymerize.PolymerizeChildModel
import com.lizl.wtmg.mvvm.model.polymerize.PolymerizeGroupModel
import com.lizl.wtmg.util.TranslateUtil
import kotlinx.android.synthetic.main.fragment_property_manager.*

class PropertyManagerFragment : BaseFragment<FragmentPropertyManagerBinding>(R.layout.fragment_property_manager)
{
    private lateinit var polymerizeGroupAdapter: PolymerizeGroupAdapter

    override fun initView()
    {
        polymerizeGroupAdapter = PolymerizeGroupAdapter()
        rv_property_category_group.adapter = polymerizeGroupAdapter
        rv_property_category_group.addItemDecoration(ListDividerItemDecoration(resources.getDimensionPixelSize(dimen.global_content_padding_content)))
    }

    override fun initData()
    {
        AppDatabase.getInstance().getPropertyDao().obAllProperty().observe(this, Observer { propertyList ->
            tv_total_property.text = propertyList.sumBy { it.amount.toInt() }.toString()
            tv_net_property.text = propertyList.sumBy { it.amount.toInt() }.toString()
            tv_total_liabilities.text = 0.toString()

            val polymerizeGroupList = mutableListOf<PolymerizeGroupModel>()

            propertyList.groupBy { it.category }.forEach { (t, u) ->
                polymerizeGroupList.add(PolymerizeGroupModel(TranslateUtil.translatePropertyCategory(t), u.sumBy { it.amount.toInt() }.toString(),
                        mutableListOf<PolymerizeChildModel>().apply {
                            u.forEach { propertyModel ->
                                add(PolymerizeChildModel(PropertyManager.getPropertyIcon(propertyModel.type),
                                        TranslateUtil.translatePropertyType(propertyModel.type), propertyModel.amount.toInt().toString(), propertyModel))
                            }
                        }))
            }
            polymerizeGroupAdapter.replaceData(polymerizeGroupList)
        })
    }

    override fun initListener()
    {
        fab_add.setOnClickListener(true) { ActivityUtils.startActivity(AddPropertyActivity::class.java) }
    }
}