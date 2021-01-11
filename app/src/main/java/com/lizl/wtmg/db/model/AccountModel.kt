package com.lizl.wtmg.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "AccountModel")
data class AccountModel(
        @PrimaryKey(autoGenerate = true)
        var id: Long = 0L,

        @ColumnInfo
        var type: String,

        @ColumnInfo
        var amount: Int)