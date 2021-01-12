package com.lizl.wtmg.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.lizl.wtmg.db.model.PropertyModel

@Dao
interface PropertyDao : BaseDao<PropertyModel>
{
    @Query("select * from PropertyModel")
    fun getAllPropertyLiveData(): LiveData<MutableList<PropertyModel>>

    @Query("select * from PropertyModel where type == :propertyType")
    fun queryPropertyByType(propertyType: String): PropertyModel?
}