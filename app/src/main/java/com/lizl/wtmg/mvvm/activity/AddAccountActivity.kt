package com.lizl.wtmg.mvvm.activity

import androidx.core.view.isVisible
import com.blankj.utilcode.util.ToastUtils
import com.lizl.wtmg.R
import com.lizl.wtmg.constant.AppConstant
import com.lizl.wtmg.custom.function.getIcon
import com.lizl.wtmg.custom.function.toAmountStr
import com.lizl.wtmg.custom.function.translate
import com.lizl.wtmg.custom.function.ui
import com.lizl.wtmg.custom.popup.PopupUtil
import com.lizl.wtmg.databinding.ActivityAddAccountBinding
import com.lizl.wtmg.db.AppDatabase
import com.lizl.wtmg.db.model.AccountModel
import com.lizl.wtmg.module.account.AccountManager
import com.lizl.wtmg.mvvm.base.BaseActivity
import com.lizl.wtmg.mvvm.model.polymerize.PolymerizeChildModel
import com.lizl.wtmg.mvvm.model.polymerize.PolymerizeModel
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
        val accountId = intent?.extras?.getLong(DATA_ACCOUNT_ID, -1L) ?: -1L
        val accountType = intent?.extras?.getString(DATA_ACCOUNT_TYPE).orEmpty()

        var accountModel: AccountModel? = null

        if (accountId != -1L && accountType.isNotBlank())
        {
            dataBinding.ctbTitle.setTitle(getString(R.string.modify_account))

            this.accountType = accountType

            accountModel = AppDatabase.getInstance().getAccountDao().queryAccountByType(accountType)
        }

        if (accountModel != null)
        {
            dataBinding.layoutAccountAmount.setEditText(accountModel.amount.toAmountStr())
            dataBinding.layoutTotalQuota.setEditText(accountModel.totalQuota.toAmountStr())
            dataBinding.layoutUsedQuota.setEditText(accountModel.usedQuota.toAmountStr())

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
        dataBinding.ctbTitle.setOnBackBtnClickListener { onBackPressed() }
        dataBinding.tvSave.setOnClickListener { onSaveBtnClick() }

        dataBinding.layoutAccountCategory.setOnClickListener {
            PopupUtil.showBottomListPopup(mutableListOf<PolymerizeModel>().apply {
                AccountManager.accountCategoryList.filter { it != AppConstant.MONEY_TRACES_CATEGORY_DEBT }.forEach {
                    add(PolymerizeChildModel(it.getIcon(), it.translate(), "", it))
                }
            }) {
                showAccountCategory(it.tag as String)
            }
        }

        dataBinding.layoutAccountType.setOnClickListener {
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
                dataBinding.layoutAccountAmount.isVisible = true
                dataBinding.layoutTotalQuota.isVisible = false
                dataBinding.layoutUsedQuota.isVisible = false

                showAccountType(AppConstant.ACCOUNT_TYPE_BACK_CARD)
            }
            AppConstant.ACCOUNT_CATEGORY_TYPE_CREDIT                                                ->
            {
                dataBinding.layoutAccountAmount.isVisible = false
                dataBinding.layoutTotalQuota.isVisible = true
                dataBinding.layoutUsedQuota.isVisible = true

                showAccountType(AppConstant.ACCOUNT_TYPE_ANT_CREDIT_PAY)
            }
        }

        dataBinding.layoutAccountCategory.setMainText(accountCategory.translate())
    }

    private fun showAccountType(accountType: String)
    {
        this.accountType = accountType
        dataBinding.layoutAccountType.setMainText(accountType.translate())
    }

    private fun onSaveBtnClick()
    {
        val amount = dataBinding.layoutAccountAmount.getEditText().toDoubleOrNull()
        if (dataBinding.layoutAccountAmount.isVisible && amount == null)
        {
            ToastUtils.showShort(R.string.please_input_amount)
            return
        }

        val totalQuota = dataBinding.layoutTotalQuota.getEditText().toDoubleOrNull()
        if (dataBinding.layoutTotalQuota.isVisible && totalQuota == null)
        {
            ToastUtils.showShort(R.string.please_input_total_quota)
            return
        }

        val usedQuota = dataBinding.layoutUsedQuota.getEditText().toDoubleOrNull()
        if (dataBinding.layoutUsedQuota.isVisible && usedQuota == null)
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
                accountModel.amount = amount ?: 0.0
            }
            else if (accountCategory == AppConstant.ACCOUNT_CATEGORY_TYPE_CREDIT)
            {
                accountModel.totalQuota = totalQuota ?: 0.0
                accountModel.usedQuota = usedQuota ?: 0.0
            }

            AppDatabase.getInstance().getAccountDao().insert(accountModel)

            ui { onBackPressed() }
        }
    }
}