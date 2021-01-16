package com.lizl.wtmg.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ExpenditureModel")
data class ExpenditureModel(
        @PrimaryKey(autoGenerate = true)
        var id: Long = 0L,

        @ColumnInfo
        var amonunt: Float,

        @ColumnInfo
        var expenditureType: String,

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
        var remarks: String = ""
)