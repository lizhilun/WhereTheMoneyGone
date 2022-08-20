package com.lizl.wtmg.custom.view.tracesrecord

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.core.view.isVisible
import com.blankj.utilcode.util.ToastUtils
import com.lizl.wtmg.R
import com.lizl.wtmg.custom.function.getIcon
import com.lizl.wtmg.custom.function.translate
import com.lizl.wtmg.custom.popup.PopupUtil
import kotlinx.android.synthetic.main.layout_account_transfer.view.*
import skin.support.widget.SkinCompatFrameLayout

class AccountTransferView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : SkinCompatFrameLayout(context, attrs, defStyleAttr) {
    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    init {
        initView()
    }

    private var outAccountType = ""
    private var inAccountType = ""

    private fun initView() {
        LayoutInflater.from(context).inflate(R.layout.layout_account_transfer, null).apply { addView(this) }

        cl_out_account.setOnClickListener {
            PopupUtil.showBottomAccountList { accountModel ->
                if (accountModel.type == inAccountType) {
                    ToastUtils.showShort(R.string.in_and_out_account_cannot_same)
                    return@showBottomAccountList
                }
                setOutAccountType(accountModel.type)
            }
        }

        cl_in_account.setOnClickListener {
            PopupUtil.showBottomAccountList { accountModel ->
                if (accountModel.type == outAccountType) {
                    ToastUtils.showShort(R.string.in_and_out_account_cannot_same)
                    return@showBottomAccountList
                }
                setInAccountType(accountModel.type)
            }
        }
    }

    fun checkSelect(): Boolean {
        if (outAccountType.isBlank()) {
            ToastUtils.showShort(R.string.please_select_out_account)
            return false
        }

        if (inAccountType.isBlank()) {
            ToastUtils.showShort(R.string.please_select_in_account)
            return false
        }

        return true
    }

    fun setOutAccountType(outAccountType: String) {
        this.outAccountType = outAccountType

        tv_out_account_des.isVisible = outAccountType.isEmpty()
        iv_out_account.isVisible = outAccountType.isNotEmpty()
        tv_out_account.isVisible = outAccountType.isNotEmpty()

        iv_out_account.setImageResource(outAccountType.getIcon())
        tv_out_account.text = outAccountType.translate()
    }

    fun setInAccountType(inAccountType: String) {
        this.inAccountType = inAccountType

        tv_in_account_des.isVisible = inAccountType.isEmpty()
        iv_in_account.isVisible = inAccountType.isNotEmpty()
        tv_in_account.isVisible = inAccountType.isNotEmpty()

        iv_in_account.setImageResource(inAccountType.getIcon())
        tv_in_account.text = inAccountType.translate()
    }

    fun getOutAccountType() = outAccountType

    fun getInAccountType() = inAccountType
}