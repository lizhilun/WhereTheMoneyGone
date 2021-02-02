package com.lizl.wtmg.custom.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.core.view.isVisible
import com.lizl.wtmg.R
import com.lizl.wtmg.mvvm.adapter.QuantityListAdapter
import com.lizl.wtmg.mvvm.model.statistics.QuantityModel
import kotlinx.android.synthetic.main.layout_quantity_statistics.view.*
import skin.support.widget.SkinCompatFrameLayout

class QuantityStatisticsView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : SkinCompatFrameLayout(context, attrs, defStyleAttr)
{
    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    private val quantityListAdapter = QuantityListAdapter()

    init
    {
        initView(attrs)
    }

    private fun initView(attrs: AttributeSet?)
    {
        LayoutInflater.from(context).inflate(R.layout.layout_quantity_statistics, null).apply {

            addView(this)

            val typeArray = context.obtainStyledAttributes(attrs, R.styleable.QuantityStatisticsView)
            tv_title.text = typeArray.getString(R.styleable.QuantityStatisticsView_title)
            typeArray.recycle()

            rv_statistics.adapter = quantityListAdapter
        }
    }

    fun setStatisticsData(quantityList: MutableList<QuantityModel>)
    {
        tv_empty_notify.isVisible = quantityList.isEmpty()
        rv_statistics.isVisible = quantityList.isNotEmpty()
        quantityListAdapter.setData(quantityList)
    }
}