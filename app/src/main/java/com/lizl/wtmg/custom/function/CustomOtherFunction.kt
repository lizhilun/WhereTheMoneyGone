package com.lizl.wtmg.custom.function

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.chad.library.adapter.base.BaseQuickAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.StringBuilder

fun String.backspace(): String
{
    return if (length <= 1) ""
    else substring(0, length - 1)
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

fun StringBuilder.clear()
{
    this.delete(0, length)
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