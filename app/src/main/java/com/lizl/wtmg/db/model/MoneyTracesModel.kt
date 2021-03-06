package com.lizl.wtmg.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "MoneyTraces")
data class MoneyTracesModel(

        @PrimaryKey(autoGenerate = true)
        var id: Long = 0L,

        @ColumnInfo
        var amount: Double = 0.0,

        @ColumnInfo
        var tracesType: String,

        @ColumnInfo
        var tracesCategory: String,

        @ColumnInfo
        var accountType: String,

        @ColumnInfo
        var recordYear: Int,

        @ColumnInfo
        var recordMonth: Int,

        @ColumnInfo
        var recordDay: Int,

        @ColumnInfo
        var recordTime: Long,

        @ColumnInfo
        var remarks: String = "",

        @ColumnInfo
        var transferToAccount: String = "")