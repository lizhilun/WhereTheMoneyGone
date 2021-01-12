package com.lizl.wtmg.mvvm.activity

import com.lizl.wtmg.R
import com.lizl.wtmg.constant.AppConstant
import com.lizl.wtmg.custom.function.ui
import com.lizl.wtmg.databinding.ActivityPropertyManagerBinding
import com.lizl.wtmg.db.AppDatabase
import com.lizl.wtmg.db.model.PropertyModel
import com.lizl.wtmg.module.property.PropertyManager
import com.lizl.wtmg.mvvm.base.BaseActivity
import kotlinx.android.synthetic.main.activity_add_property.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddPropertyActivity : BaseActivity<ActivityPropertyManagerBinding>(R.layout.activity_add_property)
{
    private var propertyType = AppConstant.PROPERTY_TYPE_BACK_CARD

    override fun initView()
    {
        tv_property_type.text = PropertyManager.getPropertyNameByType(propertyType)
    }

    override fun initListener()
    {
        ctb_title.setOnBackBtnClickListener { onBackPressed() }
        tv_save.setOnClickListener { onSaveBtnClick() }
    }

    private fun onSaveBtnClick()
    {
        val amount = et_amount.text.toString().toIntOrNull() ?: return

        GlobalScope.launch {
            var propertyModel = AppDatabase.getInstance().getPropertyDao().queryPropertyByType(propertyType)
            if (propertyModel == null)
            {
                propertyModel = PropertyModel(type = propertyType, name = PropertyManager.getPropertyNameByType(propertyType),
                        category = PropertyManager.getPropertyCategoryByType(propertyType), amount = amount, showInTotal = true)
            }
            else
            {
                propertyModel.amount += amount
            }
            AppDatabase.getInstance().getPropertyDao().insert(propertyModel)

            GlobalScope.ui { onBackPressed() }
        }
    }
}