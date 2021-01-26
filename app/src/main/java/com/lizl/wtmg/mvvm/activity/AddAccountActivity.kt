package com.lizl.wtmg.mvvm.activity

import androidx.core.view.isVisible
import com.blankj.utilcode.util.ToastUtils
import com.lizl.wtmg.R
import com.lizl.wtmg.constant.AppConstant
import com.lizl.wtmg.custom.function.getIcon
import com.lizl.wtmg.custom.function.translate
import com.lizl.wtmg.custom.function.ui
import com.lizl.wtmg.databinding.ActivityAddAccountBinding
import com.lizl.wtmg.db.AppDatabase
import com.lizl.wtmg.db.model.AccountModel
import com.lizl.wtmg.module.account.AccountManager
import com.lizl.wtmg.mvvm.base.BaseActivity
import com.lizl.wtmg.mvvm.model.polymerize.PolymerizeChildModel
import com.lizl.wtmg.mvvm.model.polymerize.PolymerizeModel
import com.lizl.wtmg.custom.popup.PopupUtil
import kotlinx.android.synthetic.main.activity_add_account.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddAccountActivity : BaseActivity<ActivityAddAccountBinding>(R.layout.activity_add_account)
{
    private var accountCategory = AppConstant.ACCOUNT_CATEGORY_TYPE_CAPITAL
    private var accountType = AppConstant.ACCOUNT_TYPE_BACK_CARD

    companion object
    {
        const val DATA_ACCOUNT_TYPE = "DATA_ACCOUNT_TYPE"
        const val DATA_ACCOUNT_ID = "DATA_ACCOUNT_ID"
    }

    override fun initData()
    {
        val accountId = intent?.extras?.getLong(DATA_ACCOUNT_ID, -1L)
        val accountType = intent?.extras?.getString(DATA_ACCOUNT_TYPE)

        var accountModel: AccountModel? = null

        if (accountId != -1L && accountType?.isNotBlank() == true)
        {
            ctb_title.setTitle(getString(R.string.modify_account))

            this.accountType = accountType

            accountModel = AppDatabase.getInstance().getAccountDao().queryAccountByType(accountType)
        }

        if (accountModel != null)
        {
            layout_account_amount.setEditText(accountModel.amount.toInt().toString())
            layout_total_quota.setEditText(accountModel.totalQuota.toInt().toString())
            layout_used_quota.setEditText(accountModel.usedQuota.toInt().toString())

            showAccountCategory(accountModel.category)
            showAccountType(accountModel.type)
        }
        else
        {
            showAccountCategory(accountCategory)
        }
    }

    override fun initListener()
    {
        ctb_title.setOnBackBtnClickListener { onBackPressed() }
        tv_save.setOnClickListener { onSaveBtnClick() }

        layout_account_category.setOnClickListener {
            PopupUtil.showBottomListPopup(mutableListOf<PolymerizeModel>().apply {
                AccountManager.accountCategoryList.forEach {
                    add(PolymerizeChildModel(it.getIcon(), it.translate(), "", it))
                }
            }) {
                showAccountCategory(it.tag as String)
            }
        }

        layout_account_type.setOnClickListener {
            PopupUtil.showBottomListPopup(mutableListOf<PolymerizeModel>().apply {
                AccountManager.getAccountListByCategory(accountCategory).forEach {
                    add(PolymerizeChildModel(it.getIcon(), it.translate(), "", it))
                }
            }) {
                showAccountType(it.tag as String)
            }
        }
    }

    private fun showAccountCategory(accountCategory: String)
    {
        this.accountCategory = accountCategory

        when (accountCategory)
        {
            AppConstant.ACCOUNT_CATEGORY_TYPE_CAPITAL, AppConstant.ACCOUNT_CATEGORY_TYPE_INVESTMENT ->
            {
                layout_account_amount.isVisible = true
                layout_total_quota.isVisible = false
                layout_used_quota.isVisible = false

                showAccountType(AppConstant.ACCOUNT_TYPE_BACK_CARD)
            }
            AppConstant.ACCOUNT_CATEGORY_TYPE_CREDIT ->
            {
                layout_account_amount.isVisible = false
                layout_total_quota.isVisible = true
                layout_used_quota.isVisible = true

                showAccountType(AppConstant.ACCOUNT_TYPE_ANT_CREDIT_PAY)
            }
        }

        layout_account_category.setMainText(accountCategory.translate())
    }

    private fun showAccountType(accountType: String)
    {
        this.accountType = accountType
        layout_account_type.setMainText(accountType.translate())
    }

    private fun onSaveBtnClick()
    {
        val amount = layout_account_amount.getEditText().toIntOrNull()
        if ((accountCategory == AppConstant.ACCOUNT_CATEGORY_TYPE_CAPITAL || accountCategory == AppConstant.ACCOUNT_CATEGORY_TYPE_INVESTMENT) && amount == null)
        {
            ToastUtils.showShort(R.string.please_input_amount)
            return
        }

        val totalQuota = layout_total_quota.getEditText().toIntOrNull()
        if (accountCategory == AppConstant.ACCOUNT_CATEGORY_TYPE_CREDIT && totalQuota == null)
        {
            ToastUtils.showShort(R.string.please_input_total_quota)
            return
        }

        val usedQuota = layout_used_quota.getEditText().toIntOrNull()
        if (accountCategory == AppConstant.ACCOUNT_CATEGORY_TYPE_CREDIT && usedQuota == null)
        {
            ToastUtils.showShort(R.string.please_input_used_quota)
            return
        }

        GlobalScope.launch {
            var accountModel = AppDatabase.getInstance().getAccountDao().queryAccountByType(accountType)
            if (accountModel == null)
            {
                accountModel = AccountModel(type = accountType, category = accountCategory, name = accountType.translate(), showInTotal = true)
            }

            if (accountCategory == AppConstant.ACCOUNT_CATEGORY_TYPE_CAPITAL || accountCategory == AppConstant.ACCOUNT_CATEGORY_TYPE_INVESTMENT)
            {
                accountModel.amount = amount?.toFloat() ?: 0F
            }
            else if (accountCategory == AppConstant.ACCOUNT_CATEGORY_TYPE_CREDIT)
            {
                accountModel.totalQuota = totalQuota?.toFloat() ?: 0F
                accountModel.usedQuota = usedQuota?.toFloat() ?: 0F
            }

            AppDatabase.getInstance().getAccountDao().insert(accountModel)

            GlobalScope.ui { onBackPressed() }
        }
    }
}