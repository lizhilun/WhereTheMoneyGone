package com.lizl.wtmg.mvvm.activity

import com.lizl.wtmg.R
import com.lizl.wtmg.constant.AppConstant
import com.lizl.wtmg.custom.function.ui
import com.lizl.wtmg.databinding.ActivityAddPropertyBinding
import com.lizl.wtmg.db.AppDatabase
import com.lizl.wtmg.db.model.PropertyAccountModel
import com.lizl.wtmg.module.property.PropertyManager
import com.lizl.wtmg.mvvm.base.BaseActivity
import com.lizl.wtmg.mvvm.model.BottomModel
import com.lizl.wtmg.util.PopupUtil
import com.lizl.wtmg.util.TranslateUtil
import kotlinx.android.synthetic.main.activity_add_property.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddPropertyActivity : BaseActivity<ActivityAddPropertyBinding>(R.layout.activity_add_property)
{
    private var propertyType = AppConstant.PROPERTY_TYPE_BACK_CARD

    override fun initView()
    {
        tv_property_type.text = TranslateUtil.translatePropertyType(propertyType)
    }

    override fun initListener()
    {
        ctb_title.setOnBackBtnClickListener { onBackPressed() }
        tv_save.setOnClickListener { onSaveBtnClick() }

        cl_property_type.setOnClickListener {
            PopupUtil.showBottomListPopup(mutableListOf<BottomModel>().apply {
                PropertyManager.getPropertyList().forEach {
                    add(BottomModel(PropertyManager.getPropertyIcon(it), TranslateUtil.translatePropertyType(it), it))
                }
            }) {
                propertyType = it.tag as String
                tv_property_type.text = TranslateUtil.translatePropertyType(propertyType)
            }
        }
    }

    private fun onSaveBtnClick()
    {
        val amount = et_amount.text.toString().toIntOrNull() ?: return

        GlobalScope.launch {
            var propertyModel = AppDatabase.getInstance().getPropertyAccountDao().queryPropertyByType(propertyType)
            if (propertyModel == null)
            {
                propertyModel = PropertyAccountModel(type = propertyType, name = TranslateUtil.translatePropertyType(propertyType), amount = amount.toFloat(),
                        showInTotal = true)
            }
            else
            {
                propertyModel.amount += amount
            }
            AppDatabase.getInstance().getPropertyAccountDao().insert(propertyModel)

            GlobalScope.ui { onBackPressed() }
        }
    }
}