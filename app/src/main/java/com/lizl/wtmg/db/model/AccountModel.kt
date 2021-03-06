package com.lizl.wtmg.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Account")
data class AccountModel(@PrimaryKey(autoGenerate = true)
                        var id: Long = 0L,

                        @ColumnInfo
                        var category: String,

                        @ColumnInfo
                        var type: String,

                        @ColumnInfo
                        var name: String,

                        @ColumnInfo
                        var showInTotal: Boolean,

                        @ColumnInfo
                        var amount: Double = 0.0,

                        @ColumnInfo
                        var totalQuota: Double = 0.0,

                        @ColumnInfo
                        var usedQuota: Double = 0.0)