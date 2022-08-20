package com.lizl.wtmg.db.dao

import androidx.room.*

@Dao
interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(element: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(elements: MutableList<T>)

    @Delete
    fun delete(element: T)

    @Update
    fun update(element: T)

    @Update
    fun updateList(elements: MutableList<T>)

    @Delete
    fun deleteList(elements: MutableList<T>)
}