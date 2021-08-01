package com.lizl.wtmg.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lizl.wtmg.UiApplication

object ViewModelUtil
{
    private val viewModelProvider: ViewModelProvider by lazy {
        ViewModelProvider(UiApplication.instance)
    }

    fun <T : ViewModel> getSharedViewModel(clazz: Class<T>) : T
    {
        return viewModelProvider.get(clazz)
    }
}