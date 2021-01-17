package com.lizl.wtmg.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CreditAccount")
data class CreditAccountModel(@PrimaryKey(autoGenerate = true) var id: Long = 0L,

        @ColumnInfo var type: String,

        @ColumnInfo var name: String,

        @ColumnInfo var showInTotal: Boolean,

        @ColumnInfo var totalQuota: Float,

        @ColumnInfo var usedQuota: Float, )