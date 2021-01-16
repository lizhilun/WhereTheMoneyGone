package com.lizl.wtmg.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.lizl.wtmg.db.model.ExpenditureModel

@Dao
interface ExpenditureDao : BaseDao<ExpenditureModel>
{
    @Query("select * from ExpenditureModel where recordMonth == :month")
    fun queryExpenditureByMonth(month: Int) : LiveData<MutableList<ExpenditureModel>>
}