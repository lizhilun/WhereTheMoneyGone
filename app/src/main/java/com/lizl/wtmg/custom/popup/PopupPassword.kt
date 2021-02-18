package com.lizl.wtmg.custom.popup

import android.content.Context
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.StringUtils
import com.lizl.wtmg.R
import com.lizl.wtmg.custom.function.getEditText
import com.lizl.wtmg.custom.function.setOnPasswordChangedListener
import com.lxj.xpopup.core.CenterPopupView
import kotlinx.android.synthetic.main.popup_password_confirm.view.*

class PopupPassword(context: Context, private val passwordOperation: Int, private val password: String? = null,
                    private val onInputFinishListener: (String) -> Unit) : CenterPopupView(context)
{

    private var firstPassword: String? = null

    private lateinit var curOperationState: OperationState

    companion object
    {
        const val PASSWORD_OPERATION_CHECK = 1
        const val PASSWORD_OPERATION_NEW = 2
        const val PASSWORD_OPERATION_MODIFY = 3
        const val PASSWORD_OPERATION_INPUT = 4
    }

    override fun getImplLayoutId() = R.layout.popup_password_confirm

    override fun onCreate()
    {
        gpv_password.setOnPasswordChangedListener { onPasswordInputFinish(it) }

        when (passwordOperation)
        {
            PASSWORD_OPERATION_CHECK -> turnToOperationState(OperationState.CheckState)
            PASSWORD_OPERATION_NEW -> turnToOperationState(OperationState.InputNewState)
            PASSWORD_OPERATION_MODIFY -> turnToOperationState(OperationState.CheckOldState)
            PASSWORD_OPERATION_INPUT -> turnToOperationState(OperationState.InputPasswordState)
        }

        showInputKeyboard()
    }

    private fun showInputKeyboard()
    {
        val editText = gpv_password.getEditText() ?: return
        gpv_password.post { KeyboardUtils.showSoftInput(editText) }
    }

    private fun turnToOperationState(operationState: OperationState)
    {
        curOperationState = operationState
        tv_notify.text = operationState.getStateNotify()
        clearInputPassword(false)
    }

    private fun turnToNextOperationState(inputPassword: String)
    {
        val nextState = curOperationState.nextState()
        if (nextState == null)
        {
            onInputFinishListener.invoke(inputPassword)
            dismiss()
        }
        else
        {
            turnToOperationState(nextState)
        }
    }

    private fun turnToWrongOperationState()
    {
        clearInputPassword(true)
        val wrongState = curOperationState.wrongState() ?: return
        firstPassword = null
        turnToOperationState(wrongState)
    }

    private fun onPasswordInputFinish(inputPassword: String)
    {
        if (curOperationState.needSaveInputPassword())
        {
            firstPassword = inputPassword
        }
        if (checkInputPassword(inputPassword))
        {
            turnToNextOperationState(inputPassword)
        }
        else
        {
            turnToWrongOperationState()
        }
    }

    private fun checkInputPassword(inputPassword: String): Boolean
    {
        return if (firstPassword.isNullOrBlank())
        {
            password.isNullOrBlank() || password == inputPassword
        }
        else
        {
            firstPassword == inputPassword
        }
    }

    private fun clearInputPassword(needDelay: Boolean)
    {
        gpv_password.postDelayed({ gpv_password.clearPassword() }, if (needDelay) 100L else 0)
    }

    override fun doAfterDismiss()
    {
        super.doAfterDismiss()

        KeyboardUtils.hideSoftInput(gpv_password)
    }

    enum class OperationState
    {
        CheckState
        {
            override fun getStateNotify() = StringUtils.getString(R.string.please_input_password_to_confirm)
        },
        CheckOldState
        {
            override fun nextState() = InputNewState

            override fun getStateNotify() = StringUtils.getString(R.string.please_input_old_password)
        },
        InputNewState
        {
            override fun nextState() = InputAgainState

            override fun getStateNotify() = StringUtils.getString(R.string.please_input_new_password)

            override fun needSaveInputPassword() = true
        },
        ReInputNewState
        {
            override fun nextState() = InputAgainState

            override fun getStateNotify() = StringUtils.getString(R.string.not_same_input_so_please_input_new_password_again)

            override fun needSaveInputPassword() = true
        },
        InputAgainState
        {
            override fun getStateNotify() = StringUtils.getString(R.string.please_input_new_password_again)

            override fun wrongState() = ReInputNewState
        },
        InputPasswordState
        {
            override fun getStateNotify() = StringUtils.getString(R.string.please_input_password_to_confirm)
        };

        open fun nextState(): OperationState? = null

        open fun wrongState(): OperationState? = null

        open fun getStateNotify() = ""

        open fun needSaveInputPassword() = false
    }
}