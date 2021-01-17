package com.lizl.wtmg.custom.function

import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.widget.EditText
import com.blankj.utilcode.util.SizeUtils

fun EditText.setHintTextSize(size: Float)
{
    val ss = SpannableString(this.hint)
    val ass = AbsoluteSizeSpan(SizeUtils.px2dp(size), true)
    ss.setSpan(ass, 0, ss.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    this.hint = SpannableString(ss)
}