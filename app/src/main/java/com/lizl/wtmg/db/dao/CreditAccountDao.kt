package com.lizl.wtmg.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.lizl.wtmg.db.model.CreditAccountModel

@Dao
interface CreditAccountDao : BaseDao<CreditAccountModel>
{
    @Query("select * from CreditAccount")
    fun obAllAccount(): LiveData<MutableList<CreditAccountModel>>

    @Query("select * from CreditAccount where type == :type")
    fun queryAccountByType(type: String): CreditAccountModel?
}