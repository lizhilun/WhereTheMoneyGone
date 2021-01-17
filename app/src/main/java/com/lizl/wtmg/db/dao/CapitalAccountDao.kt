package com.lizl.wtmg.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.lizl.wtmg.db.model.CapitalAccountModel

@Dao
interface CapitalAccountDao : BaseDao<CapitalAccountModel>
{
    @Query("select * from CapitalAccount")
    fun obAllAccount(): LiveData<MutableList<CapitalAccountModel>>

    @Query("select * from CapitalAccount")
    fun queryAllAccount(): MutableList<CapitalAccountModel>

    @Query("select * from CapitalAccount where type == :accountType")
    fun queryAccountByType(accountType: String): CapitalAccountModel?
}