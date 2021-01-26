package com.lizl.wtmg.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.lizl.wtmg.db.model.MoneyTracesModel

@Dao
interface MoneyTracesDao : BaseDao<MoneyTracesModel>
{
    @Query("select * from MoneyTraces")
    fun queryAllTraces(): MutableList<MoneyTracesModel>

    @Query("select * from MoneyTraces where recordMonth == :month order by recordTime desc")
    fun queryTracesByMonth(month: Int): LiveData<MutableList<MoneyTracesModel>>

    @Query("select * from MoneyTraces where accountType == :accountType order by recordTime desc")
    fun queryTracesByAccount(accountType: String): LiveData<MutableList<MoneyTracesModel>>

    @Query("select * from MoneyTraces")
    fun obAllTracesForBackup(): LiveData<MutableList<MoneyTracesModel>>

    @Query("DELETE FROM MoneyTraces")
    fun deleteAll()
}