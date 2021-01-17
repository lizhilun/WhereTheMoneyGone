package com.lizl.wtmg.mvvm.activity

import androidx.core.view.isVisible
import com.blankj.utilcode.util.ToastUtils
import com.lizl.wtmg.R
import com.lizl.wtmg.constant.AppConstant
import com.lizl.wtmg.custom.function.ui
import com.lizl.wtmg.databinding.ActivityAddAccountBinding
import com.lizl.wtmg.db.AppDatabase
import com.lizl.wtmg.db.model.CreditAccountModel
import com.lizl.wtmg.db.model.CapitalAccountModel
import com.lizl.wtmg.module.account.AccountManager
import com.lizl.wtmg.mvvm.base.BaseActivity
import com.lizl.wtmg.mvvm.model.BottomModel
import com.lizl.wtmg.util.PopupUtil
import com.lizl.wtmg.util.TranslateUtil
import kotlinx.android.synthetic.main.activity_add_account.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddAccountActivity : BaseActivity<ActivityAddAccountBinding>(R.layout.activity_add_account)
{
    private var accountCategory = AppConstant.ACCOUNT_CATEGORY_TYPE_CAPITAL
    private var accountType = AppConstant.ACCOUNT_TYPE_BACK_CARD

    override fun initView()
    {
        showAccountCategory(accountCategory)
    }

    override fun initListener()
    {
        ctb_title.setOnBackBtnClickListener { onBackPressed() }
        tv_save.setOnClickListener { onSaveBtnClick() }

        layout_account_category.setOnClickListener {
            PopupUtil.showBottomListPopup(mutableListOf<BottomModel>().apply {
                AccountManager.getAccountCategoryList().forEach {
                    add(BottomModel(AccountManager.getAccountCategoryIcon(it), TranslateUtil.translateAccountCategory(it), it))
                }
            }) {
                showAccountCategory(it.tag as String)
            }
        }

        layout_account_type.setOnClickListener {
            PopupUtil.showBottomListPopup(mutableListOf<BottomModel>().apply {
                AccountManager.getAccountListByCategory(accountCategory).forEach {
                    add(BottomModel(AccountManager.getAccountIcon(it), TranslateUtil.translateAccountType(it), it))
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
            AppConstant.ACCOUNT_CATEGORY_TYPE_CAPITAL ->
            {
                layout_account_amount.isVisible = true
                layout_total_quota.isVisible = false
                layout_used_quota.isVisible = false

                showAccountType(AppConstant.ACCOUNT_TYPE_BACK_CARD)
            }
            AppConstant.ACCOUNT_CATEGORY_TYPE_CREDIT  ->
            {
                layout_account_amount.isVisible = false
                layout_total_quota.isVisible = true
                layout_used_quota.isVisible = true

                showAccountType(AppConstant.ACCOUNT_TYPE_ANT_CREDIT_PAY)
            }
        }

        layout_account_category.setText(TranslateUtil.translateAccountCategory(accountCategory))
    }

    private fun showAccountType(accountType: String)
    {
        this.accountType = accountType
        layout_account_type.setText(TranslateUtil.translateAccountType(accountType))
    }

    private fun onSaveBtnClick()
    {
        when (accountCategory)
        {
            AppConstant.ACCOUNT_CATEGORY_TYPE_CAPITAL -> saveCapitalAccount()
            AppConstant.ACCOUNT_CATEGORY_TYPE_CREDIT  -> saveCreditAccount()
        }
    }

    private fun saveCapitalAccount()
    {
        val amount = layout_account_amount.getEditText().toIntOrNull()

        if (amount == null)
        {
            ToastUtils.showShort(R.string.please_input_amount)
            return
        }

        GlobalScope.launch {
            var propertyModel = AppDatabase.getInstance().getPropertyAccountDao().queryAccountByType(accountType)
            if (propertyModel == null)
            {
                propertyModel = CapitalAccountModel(type = accountType, name = TranslateUtil.translateAccountType(accountType), amount = amount.toFloat(),
                        showInTotal = true)
            }
            else
            {
                propertyModel.amount = amount.toFloat()
            }
            AppDatabase.getInstance().getPropertyAccountDao().insert(propertyModel)

            GlobalScope.ui { onBackPressed() }
        }
    }

    private fun saveCreditAccount()
    {
        val totalQuota = layout_total_quota.getEditText().toIntOrNull()
        if (totalQuota == null)
        {
            ToastUtils.showShort(R.string.please_input_total_quota)
            return
        }

        val usedQuota = layout_used_quota.getEditText().toIntOrNull()
        if (usedQuota == null)
        {
            ToastUtils.showShort(R.string.please_input_used_quota)
            return
        }

        GlobalScope.launch {
            var creditModel = AppDatabase.getInstance().getCreditAccountDao().queryAccountByType(accountType)
            if (creditModel == null)
            {
                creditModel = CreditAccountModel(type = accountType, name = TranslateUtil.translateAccountType(accountType), totalQuota = totalQuota.toFloat(),
                        usedQuota = usedQuota.toFloat(), showInTotal = true)
            }
            else
            {
                creditModel.totalQuota = totalQuota.toFloat()
                creditModel.usedQuota = usedQuota.toFloat()
            }
            AppDatabase.getInstance().getCreditAccountDao().insert(creditModel)

            GlobalScope.ui { onBackPressed() }
        }
    }
}