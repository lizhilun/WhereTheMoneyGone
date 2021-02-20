package com.lizl.wtmg.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.lizl.wtmg.db.model.MoneyTracesModel

@Dao
interface MoneyTracesDao : BaseDao<MoneyTracesModel>
{
    @Query("select * from MoneyTraces")
    fun obAllTraces(): MutableList<MoneyTracesModel>

    @Query("select * from MoneyTraces where recordYear == :year and recordMonth == :month order by recordTime desc")
    fun obTracesByMonth(year: Int, month: Int): LiveData<MutableList<MoneyTracesModel>>

    @Query("select * from MoneyTraces where recordYear == :year and recordMonth == :month order by recordTime desc")
    fun queryTracesByMonth(year: Int, month: Int): MutableList<MoneyTracesModel>

    @Query("select * from MoneyTraces where recordYear == :year order by recordTime desc")
    fun queryTracesByYear(year: Int): MutableList<MoneyTracesModel>

    @Query("select * from MoneyTraces where id == :id")
    fun queryTracesById(id: Long): MoneyTracesModel?

    @Query("select * from MoneyTraces where accountType == :accountType order by recordTime desc")
    fun obTracesByAccount(accountType: String): LiveData<MutableList<MoneyTracesModel>>

    @Query("select * from MoneyTraces")
    fun obAllTracesForBackup(): LiveData<MutableList<MoneyTracesModel>>

    @Query("DELETE FROM MoneyTraces")
    fun deleteAll()
}