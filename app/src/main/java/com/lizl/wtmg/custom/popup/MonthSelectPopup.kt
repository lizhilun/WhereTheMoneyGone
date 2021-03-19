package com.lizl.wtmg.custom.popup

import android.content.Context
import androidx.core.view.isVisible
import com.blankj.utilcode.util.ScreenUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lizl.wtmg.R
import com.lizl.wtmg.custom.function.setOnItemClickListener
import com.lizl.wtmg.mvvm.model.DateModel
import com.lxj.xpopup.core.CenterPopupView
import kotlinx.android.synthetic.main.item_month.view.*
import kotlinx.android.synthetic.main.popup_month_select.view.*

class MonthSelectPopup(context: Context, private val withAllYear: Boolean, private val listener: (year: Int, month: Int) -> Unit) : CenterPopupView(context)
{
    override fun getImplLayoutId() = R.layout.popup_month_select

    override fun getPopupWidth() = (ScreenUtils.getScreenWidth() * 0.9).toInt()

    override fun onCreate()
    {
        val curYear = DateModel().getYear()
        for (year in curYear downTo (curYear - 10))
        {
            tl_year.addTab(tl_year.newTab().setText(year.toString()))
        }

        tv_whole_year.isVisible = withAllYear

        val monthList = listOf(context.getString(R.string.January), context.getString(R.string.February), context.getString(R.string.March),
                context.getString(R.string.April), context.getString(R.string.May), context.getString(R.string.June), context.getString(R.string.July),
                context.getString(R.string.August), context.getString(R.string.September), context.getString(R.string.October),
                context.getString(R.string.November), context.getString(R.string.December))
        rv_month.adapter = MonthGridAdapter(monthList.toMutableList()).apply {
            setOnItemClickListener(false) {
                listener.invoke(curYear - tl_year.selectedTabPosition, monthList.indexOf(it) + 1)
                dismiss()
            }
        }

        tv_whole_year.setOnClickListener {
            listener.invoke(curYear - tl_year.selectedTabPosition, 0)
            dismiss()
        }
    }

    class MonthGridAdapter(monthList: MutableList<String>) : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_month, monthList)
    {
        override fun convert(helper: BaseViewHolder, item: String)
        {
            with(helper.itemView) {
                tv_month.text = item
            }
        }
    }
}
