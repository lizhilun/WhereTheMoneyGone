package com.lizl.wtmg.custom.view.titlebar

import android.view.View
import com.chad.library.adapter.base.BaseDelegateMultiAdapter
import com.chad.library.adapter.base.delegate.BaseMultiTypeDelegate
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lizl.wtmg.R
import kotlinx.android.synthetic.main.item_title_bar_image_btn.view.*
import kotlinx.android.synthetic.main.item_title_bar_text_btn.view.*

class TitleBarBtnListAdapter(btnList: List<TitleBarBtnBean.BaseBtnBean>) :
    BaseDelegateMultiAdapter<TitleBarBtnBean.BaseBtnBean, TitleBarBtnListAdapter.ViewHolder>(btnList.toMutableList())
{

    companion object
    {
        const val ITEM_TYPE_IMAGE = 1
        const val ITEM_TYPE_TEXT = 2
    }

    init
    {
        setMultiTypeDelegate(object : BaseMultiTypeDelegate<TitleBarBtnBean.BaseBtnBean>()
        {
            override fun getItemType(data: List<TitleBarBtnBean.BaseBtnBean>, position: Int): Int
            {
                return when (data[position])
                {
                    is TitleBarBtnBean.ImageBtnBean -> ITEM_TYPE_IMAGE
                    else                            -> ITEM_TYPE_TEXT
                }
            }
        })

        getMultiTypeDelegate()?.let {
            it.addItemType(ITEM_TYPE_IMAGE, R.layout.item_title_bar_image_btn)
            it.addItemType(ITEM_TYPE_TEXT, R.layout.item_title_bar_text_btn)
        }
    }

    override fun convert(helper: ViewHolder, item: TitleBarBtnBean.BaseBtnBean)
    {
        when (helper.itemViewType)
        {
            ITEM_TYPE_IMAGE -> helper.bindImageHolder(item as TitleBarBtnBean.ImageBtnBean)
            ITEM_TYPE_TEXT -> helper.bindTextHolder(item as TitleBarBtnBean.TextBtnBean)
        }
    }

    inner class ViewHolder(itemView: View) : BaseViewHolder(itemView)
    {
        fun bindImageHolder(imageBtnItem: TitleBarBtnBean.ImageBtnBean)
        {
            itemView.iv_image_btn.setImageResource(imageBtnItem.imageRedId)
            itemView.setOnClickListener { imageBtnItem.onBtnClickListener.invoke() }
        }

        fun bindTextHolder(textBtnItem: TitleBarBtnBean.TextBtnBean)
        {
            itemView.tv_text_btn.text = textBtnItem.text
            itemView.setOnClickListener { textBtnItem.onBtnClickListener.invoke() }
        }
    }
}