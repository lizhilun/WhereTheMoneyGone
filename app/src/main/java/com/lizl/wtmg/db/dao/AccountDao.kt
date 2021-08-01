package com.lizl.wtmg.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.lizl.wtmg.db.model.AccountModel
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao : BaseDao<AccountModel>
{
    @Query("select * from Account")
    fun obAllAccount(): Flow<MutableList<AccountModel>>

    @Query("select * from Account")
    fun queryAllAccount(): MutableList<AccountModel>

    @Query("select * from Account where type == :accountType")
    fun queryAccountByType(accountType: String): AccountModel?

    @Query("select * from Account")
    fun obAllAccountForBackup(): LiveData<MutableList<AccountModel>>

    @Query("DELETE FROM Account")
    fun deleteAll()
}