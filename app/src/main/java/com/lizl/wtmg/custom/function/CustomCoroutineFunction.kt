package com.lizl.wtmg.custom.function

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

fun LifecycleOwner.ui(block: suspend CoroutineScope.() -> Unit): Job
{
    return lifecycleScope.launch(Dispatchers.Main, block = block)
}

fun LifecycleOwner.io(block: suspend CoroutineScope.() -> Unit): Job
{
    return lifecycleScope.launch(Dispatchers.IO, block = block)
}

fun LifecycleOwner.launch(block: suspend CoroutineScope.() -> Unit): Job
{
    return lifecycleScope.launch(block = block)
}

fun ViewModel.io(block: suspend CoroutineScope.() -> Unit): Job
{
    return viewModelScope.launch(Dispatchers.IO, block = block)
}

fun ViewModel.launch(block: suspend CoroutineScope.() -> Unit): Job
{
    return viewModelScope.launch(block = block)
}

fun launchDefault(block: suspend CoroutineScope.() -> Unit): Job
{
    return GlobalScope.launch(block = block)
}

fun launchMain(block: suspend CoroutineScope.() -> Unit): Job
{
    return GlobalScope.launch(Dispatchers.Main, block = block)
}

fun launchIO(block: suspend CoroutineScope.() -> Unit): Job
{
    return GlobalScope.launch(Dispatchers.IO, block = block)
}