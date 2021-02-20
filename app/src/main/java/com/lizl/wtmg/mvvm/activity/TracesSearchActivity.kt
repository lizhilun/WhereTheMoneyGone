package com.lizl.wtmg.mvvm.activity

import androidx.core.widget.addTextChangedListener
import com.lizl.wtmg.R
import com.lizl.wtmg.R.dimen
import com.lizl.wtmg.custom.function.translate
import com.lizl.wtmg.custom.function.ui
import com.lizl.wtmg.custom.popup.search.PopupSearchTime
import com.lizl.wtmg.custom.popup.PopupUtil
import com.lizl.wtmg.custom.view.ListDividerItemDecoration
import com.lizl.wtmg.databinding.ActivityTracesSearchBinding
import com.lizl.wtmg.db.AppDatabase
import com.lizl.wtmg.db.model.MoneyTracesModel
import com.lizl.wtmg.module.account.AccountManager
import com.lizl.wtmg.mvvm.adapter.PolymerizeGroupAdapter
import com.lizl.wtmg.mvvm.base.BaseActivity
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import kotlinx.android.synthetic.main.activity_traces_search.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class TracesSearchActivity : BaseActivity<ActivityTracesSearchBinding>(R.layout.activity_traces_search)
{
    private lateinit var searchResultAdapter: PolymerizeGroupAdapter

    private var startTime = 0L
    private var endTime = Long.MAX_VALUE

    private var popupSearchTime: BasePopupView? = null

    override fun initView()
    {
        searchResultAdapter = PolymerizeGroupAdapter()
        rv_result.adapter = searchResultAdapter
        rv_result.addItemDecoration(ListDividerItemDecoration(resources.getDimensionPixelSize(dimen.global_content_padding_content)))
    }

    override fun initListener()
    {
        ctb_title.setOnBackBtnClickListener { onBackPressed() }

        et_keyword.addTextChangedListener { search(startTime, endTime, it?.toString().orEmpty()) }

        searchResultAdapter.setOnChildItemClickListener {
            PopupUtil.showTracesDetailPopup(it.tag as MoneyTracesModel)
        }

        cl_time.setOnClickListener {
            if (popupSearchTime == null)
            {
                popupSearchTime = XPopup.Builder(this).atView(cl_time).asCustom(PopupSearchTime(this) { type, startTime, endTime ->
                    tv_time.text = when (type)
                    {
                        PopupSearchTime.TIME_TYPE_CURRENT_MONTH -> getString(R.string.current_month)
                        PopupSearchTime.TIME_TYPE_LAST_MONTH    -> getString(R.string.last_month)
                        PopupSearchTime.TIME_TYPE_CURRENT_YEAR  -> getString(R.string.current_year)
                        PopupSearchTime.TIME_TYPE_LAST_YEAR     -> getString(R.string.last_year)
                        PopupSearchTime.TIME_TYPE_CUSTOM        -> getString(R.string.custom_time)
                        else                                    -> getString(R.string.all)
                    }
                    this.startTime = startTime
                    this.endTime = endTime
                    search(startTime, endTime, et_keyword.text.toString())
                })
            }
            popupSearchTime?.show()
        }
    }

    private var lastSearchJob: Job? = null

    private fun search(startTime: Long, endTime: Long, keyword: String)
    {
        lastSearchJob?.cancel()
        if (keyword.isBlank())
        {
            searchResultAdapter.replaceData(mutableListOf())
            return
        }
        lastSearchJob = GlobalScope.launch {
            val allTracesList = AppDatabase.getInstance().getMoneyTracesDao().queryTracesInTime(startTime, endTime).filter {
                it.tracesType.translate().contains(keyword) || it.remarks.contains(keyword)
            }.toMutableList()
            val polymerizeGroupList = AccountManager.polymerizeTrancesList(allTracesList)
            GlobalScope.ui { searchResultAdapter.replaceData(polymerizeGroupList) }
        }
    }
}