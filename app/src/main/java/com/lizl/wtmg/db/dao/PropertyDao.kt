package com.lizl.wtmg.db.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Query
import com.lizl.wtmg.db.model.PropertyModel

@Dao
interface PropertyDao : BaseDao<PropertyModel>
{
    @Query("select * from PropertyModel")
    fun obAllProperty(): LiveData<MutableList<PropertyModel>>

    @Query("select * from PropertyModel")
    fun queryAllProperty(): MutableList<PropertyModel>

    @Query("select * from PropertyModel where type == :propertyType")
    fun queryPropertyByType(propertyType: String): PropertyModel?

    @Query("select category from PropertyModel")
    fun obPropertyCategoryList(): LiveData<List<String>>

    @Query("select * from PropertyModel where category == :category")
    fun obPropertyByCategory(category: String): LiveData<MutableList<PropertyModel>>
}