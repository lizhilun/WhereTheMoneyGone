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
    private var maxProgress = 0.0

    fun setData(data: MutableList<QuantityModel>)
    {
        maxPositive = data.maxOfOrNull { it.quantity } ?: 0.0
        minComplex = data.minOfOrNull { it.quantity } ?: 0.0
        maxProgress = abs(maxPositive).coerceAtLeast(abs(minComplex))
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
                    npb_positive.max = maxProgress.toInt()
                    npb_positive.progress = item.quantity.toInt()
                }
                else
                {
                    npb_positive.isVisible = false
                    npb_complex.isVisible = true
                    npb_complex.max = maxProgress.toInt()
                    npb_complex.progress = (maxProgress + item.quantity).toInt()
                }
            }
            else
            {
                npb_complex.isVisible = false
                npb_positive.isVisible = true
                npb_positive.max = maxProgress.toInt()
                npb_positive.progress = item.quantity.toInt()
            }
        }
    }
}