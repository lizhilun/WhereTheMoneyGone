package com.lizl.wtmg.mvvm.activity

import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.lizl.wtmg.R
import com.lizl.wtmg.R.dimen
import com.lizl.wtmg.constant.AppConstant
import com.lizl.wtmg.custom.function.toAmountStr
import com.lizl.wtmg.custom.function.translate
import com.lizl.wtmg.custom.function.ui
import com.lizl.wtmg.custom.popup.search.PopupSearchTime
import com.lizl.wtmg.custom.popup.PopupUtil
import com.lizl.wtmg.custom.popup.search.PopupSearchCondition
import com.lizl.wtmg.custom.view.ListDividerItemDecoration
import com.lizl.wtmg.databinding.ActivityTracesSearchBinding
import com.lizl.wtmg.db.AppDatabase
import com.lizl.wtmg.db.model.MoneyTracesModel
import com.lizl.wtmg.module.account.AccountDataManager
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

    private var minAmount = Int.MIN_VALUE
    private var maxAmount = Int.MAX_VALUE

    private var popupSearchTime: BasePopupView? = null
    private var popupSearchCondition: BasePopupView? = null

    override fun initView()
    {
        searchResultAdapter = PolymerizeGroupAdapter()
        rv_result.adapter = searchResultAdapter
        rv_result.addItemDecoration(ListDividerItemDecoration(resources.getDimensionPixelSize(dimen.global_content_padding_content)))
    }

    override fun initListener()
    {
        ctb_title.setOnBackBtnClickListener { onBackPressed() }

        et_keyword.addTextChangedListener { search(keyword = it?.toString().orEmpty()) }

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
                    search()
                })
            }
            popupSearchTime?.show()
        }

        cl_condition.setOnClickListener {
            if (popupSearchCondition == null)
            {
                popupSearchCondition = XPopup.Builder(this).atView(cl_condition).asCustom(PopupSearchCondition(this) { minAmount, maxAmount ->
                    this.minAmount = minAmount
                    this.maxAmount = maxAmount
                    search()
                })
            }
            popupSearchCondition?.show()
        }
    }

    private var lastSearchOb: LiveData<MutableList<MoneyTracesModel>>? = null

    private fun search(startTime: Long = this.startTime, endTime: Long = this.endTime, keyword: String = et_keyword.text.toString(),
                       minAmount: Int = this.minAmount, maxAmount: Int = this.maxAmount)
    {
        lastSearchOb?.removeObservers(this)
        if (keyword.isBlank())
        {
            tv_result.text = ""
            searchResultAdapter.replaceData(mutableListOf())
            return
        }

        val searchSql = SimpleSQLiteQuery(getSearchSql(keyword, startTime, endTime, minAmount, maxAmount))
        lastSearchOb = AppDatabase.getInstance().getMoneyTracesDao().searchAndOb(searchSql).apply {
            observe(this@TracesSearchActivity, Observer { allTracesList ->
                val resultStringBuffer = StringBuffer()
                resultStringBuffer.append(getString(R.string.search_result_count_is, allTracesList.size))
                if (allTracesList.isNotEmpty())
                {
                    val income = allTracesList.filter { it.tracesCategory == AppConstant.MONEY_TRACES_CATEGORY_INCOME }.sumByDouble { it.amount }.toAmountStr()
                    val expenditure =
                            allTracesList.filter { it.tracesCategory == AppConstant.MONEY_TRACES_CATEGORY_EXPENDITURE }.sumByDouble { it.amount }.toAmountStr()
                    resultStringBuffer.append("\n").append(getString(R.string.income)).append("：").append(income).append("    ")
                        .append(getString(R.string.expenditure)).append("：").append(expenditure)
                }

                GlobalScope.ui { tv_result.text = resultStringBuffer.toString() }

                val polymerizeGroupList = AccountManager.polymerizeTrancesList(allTracesList)
                GlobalScope.ui { searchResultAdapter.setDiffNewData(polymerizeGroupList) }
            })
        }
    }

    private fun getSearchSql(keyword: String, startTime: Long, endTime: Long, minAmount: Int, maxAmount: Int): String
    {
        val sqlStringBuffer = StringBuffer()

        sqlStringBuffer.append("select * from MoneyTraces").append(" where ").append("recordTime >= $startTime and recordTime <= $endTime").append(" and ")
            .append("amount >= $minAmount and amount <= $maxAmount")

        if (keyword.isNotBlank())
        {
            sqlStringBuffer.append(" and ").append("(").append("remarks like '%$keyword%'")

            val allTracesTypeList = mutableListOf<String>().apply {
                addAll(AccountManager.expenditureTypeList)
                addAll(AccountManager.incomeTypeList)
                addAll(AccountManager.debtTypeList)
            }
            allTracesTypeList.filter { it.translate().contains(keyword) }.forEach {
                sqlStringBuffer.append(" or ").append("tracesType == '$it'")
            }

            sqlStringBuffer.append(")")
        }

        sqlStringBuffer.append(" order by recordTime desc")

        return sqlStringBuffer.toString()
    }
}