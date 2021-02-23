package com.lizl.wtmg.custom.view.tracesrecord

import android.accounts.Account
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.blankj.utilcode.util.ToastUtils
import com.lizl.wtmg.R
import com.lizl.wtmg.constant.AppConstant
import com.lizl.wtmg.custom.function.setOnClickListener
import com.lizl.wtmg.custom.function.ui
import com.lizl.wtmg.custom.popup.PopupUtil
import com.lizl.wtmg.db.AppDatabase
import kotlinx.android.synthetic.main.layout_borrow_money.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import skin.support.widget.SkinCompatFrameLayout

class BorrowMoneyView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : SkinCompatFrameLayout(context, attrs, defStyleAttr)
{
    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    private var borrowType = AppConstant.BORROW_TYPE_BORROW_OUT

    init
    {
        initView()
    }

    private lateinit var borrowTypeBtnList: List<View>

    private fun initView()
    {
        LayoutInflater.from(context).inflate(R.layout.layout_borrow_money, null).apply { addView(this) }

        tv_borrow_out.tag = AppConstant.BORROW_TYPE_BORROW_OUT
        tv_borrow_in.tag = AppConstant.BORROW_TYPE_BORROW_IN
        tv_payback_out.tag = AppConstant.BORROW_TYPE_PAY_BACK_OUT
        tv_payback_in.tag = AppConstant.BORROW_TYPE_PAY_BACK_IN

        borrowTypeBtnList = listOf(tv_borrow_out, tv_borrow_in, tv_payback_out, tv_payback_in)

        val onBorrowTypeClick = { view: View ->
            borrowTypeBtnList.forEach { it.isSelected = it == view }
            borrowType = view.tag.toString()
            updateAccountInputView()
        }

        borrowTypeBtnList.forEach {
            it.setOnClickListener(true) { view: View ->
                onBorrowTypeClick.invoke(view)
            }
        }

        layout_out_account.setOnLayoutClickListener {
            PopupUtil.showBottomAccountList { accountModel ->
                layout_out_account.setAccountType(accountModel.type)
            }
        }

        layout_in_account.setOnLayoutClickListener {
            PopupUtil.showBottomAccountList { accountModel ->
                layout_in_account.setAccountType(accountModel.type)
            }
        }

        setBorrowType(borrowType)
    }

    private fun updateAccountInputView()
    {
        when (borrowType)
        {
            AppConstant.BORROW_TYPE_BORROW_IN ->
            {
                layout_out_account.setInputEnable(true)
                layout_out_account.setHint(context.getString(R.string.borrower))

                layout_in_account.setInputEnable(false)
                layout_in_account.setHint(context.getString(R.string.transfer_in_account))
            }
            AppConstant.BORROW_TYPE_PAY_BACK_IN ->
            {
                layout_out_account.setInputEnable(true)
                layout_out_account.setHint(context.getString(R.string.payer))

                layout_in_account.setInputEnable(false)
                layout_in_account.setHint(context.getString(R.string.transfer_in_account))
            }
            AppConstant.BORROW_TYPE_PAY_BACK_OUT ->
            {
                layout_out_account.setInputEnable(false)
                layout_out_account.setHint(context.getString(R.string.transfer_out_account))

                layout_in_account.setInputEnable(true)
                layout_in_account.setHint(context.getString(R.string.payee))
            }
            else                                 ->
            {
                layout_out_account.setInputEnable(false)
                layout_out_account.setHint(context.getString(R.string.transfer_out_account))

                layout_in_account.setInputEnable(true)
                layout_in_account.setHint(context.getString(R.string.borrower))
            }
        }
    }

    fun setInAccountType(accountType: String)
    {
        setAccountType(accountType, layout_in_account)
    }

    fun setOutAccountType(outAccountType: String)
    {
        setAccountType(outAccountType, layout_out_account)
    }

    fun setBorrowType(borrowType: String)
    {
        this.borrowType = borrowType
        borrowTypeBtnList.forEach { it.isSelected = it.tag == borrowType }
        updateAccountInputView()
    }

    private fun setAccountType(accountType: String, accountInputView: AccountInputView)
    {
        GlobalScope.launch {
            AppDatabase.getInstance().getAccountDao().queryAccountByType(accountType)?.let {
                GlobalScope.ui {
                    accountInputView.setInputEnable(!it.showInTotal)
                    if (it.showInTotal)
                    {
                        accountInputView.setAccountType(accountType)
                    }
                    else
                    {
                        accountInputView.setAccountName(it.name)
                    }
                }
            }
        }
    }

    fun checkInput(): Boolean
    {
        if (layout_out_account.getInputAccountType().isBlank())
        {
            ToastUtils.showShort("${context.getString(R.string.please_input)}${layout_out_account.getHint()}")
            return false
        }

        if (layout_in_account.getInputAccountType().isBlank())
        {
            ToastUtils.showShort("${context.getString(R.string.please_input)}${layout_in_account.getHint()}")
            return false
        }

        return true
    }

    fun getBorrowInfo(): BorrowInfoModel
    {
        return BorrowInfoModel(borrowType, layout_out_account.getInputAccountType(), layout_in_account.getInputAccountType())
    }

    class BorrowInfoModel(val borrowType: String, val outAccountType: String, val inAccountType: String)
}