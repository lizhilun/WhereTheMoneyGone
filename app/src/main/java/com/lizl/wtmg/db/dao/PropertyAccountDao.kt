package com.lizl.wtmg.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.lizl.wtmg.db.model.PropertyAccountModel

@Dao
interface PropertyAccountDao : BaseDao<PropertyAccountModel>
{
    @Query("select * from PropertyAccount")
    fun obAllAccount(): LiveData<MutableList<PropertyAccountModel>>

    @Query("select * from PropertyAccount")
    fun queryAllProperty(): MutableList<PropertyAccountModel>

    @Query("select * from PropertyAccount where type == :propertyType")
    fun queryPropertyByType(propertyType: String): PropertyAccountModel?
}