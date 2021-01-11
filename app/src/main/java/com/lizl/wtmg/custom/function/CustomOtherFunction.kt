package com.lizl.wtmg.custom.function

fun String.backspace(): String
{
    return if (length <= 1) ""
    else substring(0, length - 1)
}

fun String.delete(str: String): String
{
    return replace(str, "")
}
