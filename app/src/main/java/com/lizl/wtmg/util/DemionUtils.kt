package com.lizl.wtmg.util

import com.blankj.utilcode.util.Utils

object DimensionUtils
{
    fun getDimension(resId: Int): Float
    {
        return Utils.getApp().resources.getDimension(resId)
    }
}