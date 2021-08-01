package com.lizl.wtmg.mvvm.activity

import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.sqlite.db.SimpleSQLiteQuery
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
import com.lizl.wtmg.module.account.AccountManager
import com.lizl.wtmg.mvvm.adapter.PolymerizeGroupAdapter
import com.lizl.wtmg.mvvm.base.BaseActivity
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView

class TracesSearchActivity : BaseActivity<ActivityTracesSearchBinding>(R.layout.activity_traces_search)
{
    private lateinit var searchResultAdapter: PolymerizeGroupAdapter

    private var startTime = 0L
    private var endTime = Long.MAX_VALUE

    private var minAmount = Int.MIN_VALUE
    private var maxAmount = Int.MAX_VALUE

    private var popupSearchTime: BasePopupView? = null
    private var popupSearchCondition: BasePopupView? = null

    companion object
    {
        const val DATA_START_TIME = "DATA_START_TIME"
        const val DATA_END_TIME = "DATA_END_TIME"
        const val DATA_KEY_WORD = "DATA_KEY_WORD"
    }

    override fun initView()
    {
        searchResultAdapter = PolymerizeGroupAdapter()
        dataBinding.rvResult.adapter = searchResultAdapter
        dataBinding.rvResult.addItemDecoration(ListDividerItemDecoration(resources.getDimensionPixelSize(dimen.global_content_padding_content)))
    }

    override fun initData()
    {
        intent?.extras?.let {
            startTime = it.getLong(DATA_START_TIME, startTime)
            endTime = it.getLong(DATA_END_TIME, endTime)
            val keyword = it.getString(DATA_KEY_WORD)
            if (keyword?.isNotBlank() == true)
            {
                dataBinding.etKeyword.setText(keyword)
                search()
            }
            dataBinding.tvTime.text = timeTypeToString(PopupSearchTime.TIME_TYPE_CUSTOM)
        }
    }

    override fun initListener()
    {
        dataBinding.ctbTitle.setOnBackBtnClickListener { onBackPressed() }

        dataBinding.etKeyword.addTextChangedListener { search(keyword = it?.toString().orEmpty()) }

        searchResultAdapter.setOnChildItemClickListener {
            PopupUtil.showTracesDetailPopup(it.tag as MoneyTracesModel)
        }

        dataBinding.clTime.setOnClickListener {
            if (popupSearchTime == null)
            {
                popupSearchTime = XPopup.Builder(this).atView(dataBinding.clTime).asCustom(PopupSearchTime(this) { type, startTime, endTime ->
                    dataBinding.tvTime.text = timeTypeToString(type)
                    this.startTime = startTime
                    this.endTime = endTime
                    search()
                })
            }
            popupSearchTime?.show()
        }

        dataBinding.clCondition.setOnClickListener {
            if (popupSearchCondition == null)
            {
                popupSearchCondition = XPopup.Builder(this).atView(dataBinding.clCondition).asCustom(PopupSearchCondition(this) { minAmount, maxAmount ->
                    this.minAmount = minAmount
                    this.maxAmount = maxAmount
                    search()
                })
            }
            popupSearchCondition?.show()
        }
    }

    private fun timeTypeToString(timeType: Int): String
    {
        return when (timeType)
        {
            PopupSearchTime.TIME_TYPE_CURRENT_MONTH -> getString(R.string.current_month)
            PopupSearchTime.TIME_TYPE_LAST_MONTH    -> getString(R.string.last_month)
            PopupSearchTime.TIME_TYPE_CURRENT_YEAR  -> getString(R.string.current_year)
            PopupSearchTime.TIME_TYPE_LAST_YEAR     -> getString(R.string.last_year)
            PopupSearchTime.TIME_TYPE_CUSTOM        -> getString(R.string.custom_time)
            else                                    -> getString(R.string.all)
        }
    }

    private var lastSearchOb: LiveData<MutableList<MoneyTracesModel>>? = null

    private fun search(startTime: Long = this.startTime,
                       endTime: Long = this.endTime,
                       keyword: String = dataBinding.etKeyword.text.toString(),
                       minAmount: Int = this.minAmount,
                       maxAmount: Int = this.maxAmount)
    {
        lastSearchOb?.removeObservers(this)
        if (keyword.isBlank())
        {
            dataBinding.tvResult.text = ""
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
                    resultStringBuffer.append("\n")
                        .append(getString(R.string.income))
                        .append("：")
                        .append(income)
                        .append("    ")
                        .append(getString(R.string.expenditure))
                        .append("：")
                        .append(expenditure)
                }

                ui { dataBinding.tvResult.text = resultStringBuffer.toString() }

                val polymerizeGroupList = AccountManager.polymerizeTrancesList(allTracesList)
                ui { searchResultAdapter.setDiffNewData(polymerizeGroupList) }
            })
        }
    }

    private fun getSearchSql(keyword: String, startTime: Long, endTime: Long, minAmount: Int, maxAmount: Int): String
    {
        val sqlStringBuffer = StringBuffer()

        sqlStringBuffer.append("select * from MoneyTraces")
            .append(" where ")
            .append("recordTime >= $startTime and recordTime <= $endTime")
            .append(" and ")
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