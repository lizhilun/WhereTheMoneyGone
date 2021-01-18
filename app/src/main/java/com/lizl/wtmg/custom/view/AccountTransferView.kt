package com.lizl.wtmg.custom.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.core.view.isVisible
import com.blankj.utilcode.util.ToastUtils
import com.lizl.wtmg.R
import com.lizl.wtmg.custom.function.getIcon
import com.lizl.wtmg.custom.function.translate
import com.lizl.wtmg.util.PopupUtil
import kotlinx.android.synthetic.main.layout_account_transfer.view.*
import skin.support.widget.SkinCompatFrameLayout

class AccountTransferView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : SkinCompatFrameLayout(context, attrs, defStyleAttr)
{
    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    init
    {
        initView()
    }

    private var outAccountType = ""
    private var inAccountType = ""

    private fun initView()
    {
        LayoutInflater.from(context).inflate(R.layout.layout_account_transfer, null).apply { addView(this) }

        cl_out_account.setOnClickListener {
            PopupUtil.showBottomAccountList { accountModel ->
                if (accountModel.type == inAccountType)
                {
                    ToastUtils.showShort(R.string.in_and_out_account_cannot_same)
                    return@showBottomAccountList
                }
                outAccountType = accountModel.type

                tv_out_account_des.isVisible = false
                iv_out_account.isVisible = true
                tv_out_account.isVisible = true

                iv_out_account.setImageResource(accountModel.type.getIcon())
                tv_out_account.text = accountModel.type.translate()
            }
        }

        cl_in_account.setOnClickListener {
            PopupUtil.showBottomAccountList { accountModel ->
                if (accountModel.type == outAccountType)
                {
                    ToastUtils.showShort(R.string.in_and_out_account_cannot_same)
                    return@showBottomAccountList
                }
                inAccountType = accountModel.type

                tv_in_account_des.isVisible = false
                iv_in_account.isVisible = true
                tv_in_account.isVisible = true

                iv_in_account.setImageResource(accountModel.type.getIcon())
                tv_in_account.text = accountModel.type.translate()
            }
        }
    }

    fun checkSelect(): Boolean
    {
        if (outAccountType.isBlank())
        {
            ToastUtils.showShort(R.string.please_select_out_account)
            return false
        }

        if (inAccountType.isBlank())
        {
            ToastUtils.showShort(R.string.please_select_in_account)
            return false
        }

        return true
    }

    fun getOutAccountType() = outAccountType

    fun getInAccountType() = inAccountType
}