package com.lizl.wtmg.mvvm.adapter

import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lizl.wtmg.R
import com.lizl.wtmg.custom.function.launchDefault
import com.lizl.wtmg.custom.function.launchMain
import com.lizl.wtmg.custom.popup.PopupUtil
import com.lizl.wtmg.mvvm.model.setting.*
import kotlinx.android.synthetic.main.item_setting_boolean.view.*
import kotlinx.android.synthetic.main.item_setting_normal.view.*
import kotlinx.android.synthetic.main.item_setting_value.view.*

class SettingListAdapter(settingList: MutableList<BaseSettingModel>) : BaseDBMultiAdapter<BaseSettingModel, BaseViewHolder>(settingList)
{

    companion object
    {
        private const val ITEM_TYPE_DIVIDE = 1
        private const val ITEM_TYPE_NORMAL = 2
        private const val ITEM_TYPE_BOOLEAN = 3
        private const val ITEM_TYPE_STRING_RADIO = 4
    }

    override fun registerItemType(item: BaseSettingModel) = when (item)
    {
        is BooleanSettingModel     -> ITEM_TYPE_BOOLEAN
        is NormalSettingModel      -> ITEM_TYPE_NORMAL
        is StringRadioSettingModel -> ITEM_TYPE_STRING_RADIO
        else                       -> ITEM_TYPE_DIVIDE
    }

    override fun registerItemLayout() = mutableListOf<Pair<Int, Int>>().apply {
        add(Pair(ITEM_TYPE_DIVIDE, R.layout.item_setting_divide))
        add(Pair(ITEM_TYPE_BOOLEAN, R.layout.item_setting_boolean))
        add(Pair(ITEM_TYPE_NORMAL, R.layout.item_setting_normal))
        add(Pair(ITEM_TYPE_STRING_RADIO, R.layout.item_setting_value))
    }

    override fun convert(helper: BaseViewHolder, item: BaseSettingModel)
    {
        when (item)
        {
            is BooleanSettingModel     -> bindBooleanViewHolder(helper, item)
            is NormalSettingModel      -> bindNormalViewHolder(helper, item)
            is StringRadioSettingModel -> bindRadioViewHolder(helper, item)
        }
    }

    override fun convert(helper: BaseViewHolder, item: BaseSettingModel, payloads: List<Any>)
    {
        convert(helper, item)
    }

    private fun bindBooleanViewHolder(helper: BaseViewHolder, settingModel: BooleanSettingModel)
    {
        with(helper.itemView) {

            settingModel.icon?.let { iv_boolean_icon.setImageResource(it) }

            tv_boolean_setting_name.text = settingModel.name
            iv_boolean_setting_checked.isSelected = settingModel.getValue()

            setOnClickListener {
                launchDefault {
                    if (settingModel.autoSave)
                    {
                        settingModel.saveValue(!settingModel.getValue())
                    }
                    launchMain {
                        val isChecked = iv_boolean_setting_checked.isSelected
                        if (settingModel.autoSave)
                        {
                            iv_boolean_setting_checked.isSelected = !isChecked
                        }
                        settingModel.callback?.invoke(!isChecked)
                    }
                }
            }
        }
    }

    private fun bindNormalViewHolder(helper: BaseViewHolder, settingModel: NormalSettingModel)
    {
        with(helper.itemView) {

            settingModel.icon?.let { iv_normal_icon.setImageResource(it) }

            tv_normal_setting_name.text = settingModel.name
            tv_normal_setting_value.text = settingModel.value

            setOnClickListener { settingModel.callback.invoke(settingModel) }
        }
    }

    private fun bindRadioViewHolder(helper: BaseViewHolder, settingModel: RadioSettingModel<*, *>)
    {
        with(helper.itemView) {

            settingModel.icon?.let { iv_value_icon.setImageResource(it) }

            tv_value_setting_name.text = settingModel.name

            val showValue = when (settingModel)
            {
                is StringRadioSettingModel -> settingModel.radioMap.getValue(settingModel.getValue())
                else                       -> ""
            }

            tv_value_setting_value.text = showValue

            val radioList = mutableListOf<String>()
            settingModel.radioMap.forEach { radioList.add(it.value) }

            setOnClickListener {
                PopupUtil.showRadioGroupPopup(settingModel.name, radioList, showValue) { result ->
                    settingModel.radioMap.filterValues { it == result }.keys.forEach {
                        if (settingModel is StringRadioSettingModel)
                        {
                            launchDefault {
                                settingModel.saveValue(it as String)
                                launchMain {
                                    setData(data.indexOf(settingModel), settingModel)
                                    settingModel.callback.invoke(settingModel)
                                }
                            }
                        }
                        return@showRadioGroupPopup
                    }
                }
            }
        }
    }
}