package com.lizl.wtmg.custom.function

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