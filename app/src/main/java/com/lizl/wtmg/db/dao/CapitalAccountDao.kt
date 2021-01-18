package com.lizl.wtmg.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.lizl.wtmg.db.model.AccountModel

@Dao
interface CapitalAccountDao : BaseDao<AccountModel>
{
    @Query("select * from Account")
    fun obAllAccount(): LiveData<MutableList<AccountModel>>

    @Query("select * from Account")
    fun queryAllAccount(): MutableList<AccountModel>

    @Query("select * from Account where type == :accountType")
    fun queryAccountByType(accountType: String): AccountModel?
}