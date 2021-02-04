package com.lizl.wtmg.custom.function

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.chad.library.adapter.base.BaseQuickAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

fun StringBuilder.backspace()
{
    if (length == 0)
    {
        return
    }
    deleteAt(length - 1)
}

fun Double.toAmountStr(): String
{
    val doubleStr = "%.2f".format(this)
    val oneAfterPoint = doubleStr[doubleStr.lastIndex - 1]
    val twoAfterPoint = doubleStr.last()
    return if (twoAfterPoint == '0')
    {
        if (oneAfterPoint == '0') this.toInt().toString()
        else  "%.1f".format(this)
    }
    else
    {
        "%.2f".format(this)
    }
}

fun String.delete(str: String): String
{
    return replace(str, "")
}

fun GlobalScope.ui(runnable: () -> Unit)
{
    GlobalScope.launch(Dispatchers.Main) {
        runnable.invoke()
    }
}

fun <T> BaseQuickAdapter<T, *>.update(model: T)
{
    data.indexOf(model).let {
        if (it < 0) return
        setData(it, model)
    }
}

fun Uri.getFileName(context: Context): String?
{
    val cursor = context.contentResolver.query(this, null, null, null, null, null)

    var fileName: String? = null
    cursor?.use {
        if (it.moveToFirst())
        {
            fileName = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
        }
    }
    return fileName
}