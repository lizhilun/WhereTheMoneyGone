package com.lizl.wtmg.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.lizl.wtmg.db.model.MoneyTracesModel

@Dao
interface MoneyTracesDao : BaseDao<MoneyTracesModel>
{
    @Query("select * from MoneyTraces where recordMonth == :month")
    fun queryTracesByMonth(month: Int): LiveData<MutableList<MoneyTracesModel>>

    @Query("select * from MoneyTraces where accountType == :accountType")
    fun queryTracesByAccount(accountType: String): LiveData<MutableList<MoneyTracesModel>>
}