package com.lizl.wtmg.mvvm.fragment

import androidx.lifecycle.Observer
import com.blankj.utilcode.util.ActivityUtils
import com.lizl.wtmg.R
import com.lizl.wtmg.R.dimen
import com.lizl.wtmg.constant.AppConstant
import com.lizl.wtmg.custom.function.setOnClickListener
import com.lizl.wtmg.custom.view.ListDividerItemDecoration
import com.lizl.wtmg.databinding.FragmentPropertyManagerBinding
import com.lizl.wtmg.db.AppDatabase
import com.lizl.wtmg.db.model.CreditAccountModel
import com.lizl.wtmg.db.model.PropertyAccountModel
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

    private val propertyLiveData = AppDatabase.getInstance().getPropertyAccountDao().obAllAccount()
    private val creditLiveData = AppDatabase.getInstance().getCreditAccountDao().obAllAccount()

    override fun initView()
    {
        polymerizeGroupAdapter = PolymerizeGroupAdapter()
        rv_property_category_group.adapter = polymerizeGroupAdapter
        rv_property_category_group.addItemDecoration(ListDividerItemDecoration(resources.getDimensionPixelSize(dimen.global_content_padding_content)))
    }

    override fun initData()
    {
        propertyLiveData.observe(this, Observer { onAccountDataUpdate() })

        creditLiveData.observe(this, Observer { onAccountDataUpdate() })
    }

    override fun initListener()
    {
        fab_add.setOnClickListener(true) { ActivityUtils.startActivity(AddPropertyActivity::class.java) }
    }

    private fun onAccountDataUpdate()
    {
        val propertyList = propertyLiveData.value ?: mutableListOf()
        val creditList = creditLiveData.value ?: mutableListOf()

        tv_total_property.text = propertyList.sumBy { it.amount.toInt() }.toString()
        tv_net_property.text = propertyList.sumBy { it.amount.toInt() }.toString()
        tv_total_liabilities.text = creditList.sumBy { it.usedQuota.toInt() }.toString()

        val polymerizeGroupList = mutableListOf<PolymerizeGroupModel>()
        if (creditList.isNotEmpty())
        {
            polymerizeGroupList.add(creditListToPolymerizeGroup(creditList))
        }
        if (propertyList.isNotEmpty())
        {
            polymerizeGroupList.add(propertyListToPolymerizeGroup(propertyList))
        }
        polymerizeGroupAdapter.replaceData(polymerizeGroupList)
    }

    private fun propertyListToPolymerizeGroup(propertyList: List<PropertyAccountModel>): PolymerizeGroupModel
    {
        return PolymerizeGroupModel(TranslateUtil.translatePropertyCategory(AppConstant.PROPERTY_CATEGORY_TYPE_CAPITAL),
                propertyList.sumBy { it.amount.toInt() }.toString(), mutableListOf<PolymerizeChildModel>().apply {
            propertyList.forEach { propertyModel ->
                add(PolymerizeChildModel(PropertyManager.getPropertyIcon(propertyModel.type), TranslateUtil.translatePropertyType(propertyModel.type),
                        propertyModel.amount.toInt().toString(), propertyModel))
            }
        })
    }

    private fun creditListToPolymerizeGroup(creditList: List<CreditAccountModel>): PolymerizeGroupModel
    {
        return PolymerizeGroupModel(TranslateUtil.translatePropertyCategory(AppConstant.PROPERTY_CATEGORY_TYPE_CREDIT),
                creditList.sumBy { it.usedQuota.toInt() }.toString(), mutableListOf<PolymerizeChildModel>().apply {
            creditList.forEach { propertyModel ->
                add(PolymerizeChildModel(PropertyManager.getPropertyIcon(propertyModel.type), TranslateUtil.translatePropertyType(propertyModel.type),
                        propertyModel.usedQuota.toInt().toString(), propertyModel))
            }
        })
    }
}