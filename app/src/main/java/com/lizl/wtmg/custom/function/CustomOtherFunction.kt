package com.lizl.wtmg.custom.function

import com.chad.library.adapter.base.BaseQuickAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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