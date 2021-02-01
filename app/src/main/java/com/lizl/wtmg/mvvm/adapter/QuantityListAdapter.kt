package com.lizl.wtmg.mvvm.adapter

import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lizl.wtmg.R
import com.lizl.wtmg.custom.function.getIcon
import com.lizl.wtmg.mvvm.model.statistics.QuantityModel
import kotlinx.android.synthetic.main.item_quantity_statistics.view.*
import kotlin.math.abs

class QuantityListAdapter : BaseQuickAdapter<QuantityModel, BaseViewHolder>(R.layout.item_quantity_statistics)
{
    private var maxPositive = 0.0
    private var minComplex = 0.0

    fun setData(data: MutableList<QuantityModel>)
    {
        maxPositive = data.maxOf { it.quantity }
        minComplex = data.minOf { it.quantity }
        super.setNewData(data)
    }

    override fun convert(helper: BaseViewHolder, item: QuantityModel)
    {
        with(helper.itemView) {

            iv_icon.setImageResource(item.name.getIcon())
            tv_quantity.text = item.quantity.toInt().toString()

            if (minComplex < 0)
            {
                if (item.quantity > 0)
                {
                    npb_complex.isInvisible = true
                    npb_positive.isVisible = true
                    npb_positive.max = maxPositive.toInt()
                    npb_positive.progress = item.quantity.toInt()
                }
                else
                {
                    npb_positive.isVisible = false
                    npb_complex.isVisible = true
                    npb_complex.max = abs(minComplex).toInt()
                    npb_complex.progress = (item.quantity - minComplex).toInt()
                }
            }
            else
            {
                npb_complex.isVisible = false
                npb_positive.isVisible = true
                npb_positive.max = maxPositive.toInt()
                npb_positive.progress = item.quantity.toInt()
            }
        }
    }
}