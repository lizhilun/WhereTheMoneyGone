package com.lizl.wtmg.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "incomeModel")
data class IncomeModel(
        @PrimaryKey(autoGenerate = true)
        var id: Long = 0L,

        @ColumnInfo
        var amonunt: Float,

        @ColumnInfo
        var incomeType: String,

        @ColumnInfo
        var time: Long)