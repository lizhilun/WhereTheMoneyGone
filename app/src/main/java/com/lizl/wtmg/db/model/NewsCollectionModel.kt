package com.lizl.wtmg.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "newsCollections")
data class NewsCollectionModel(@PrimaryKey(autoGenerate = true)
                          var id: Long = 0L,

                          @ColumnInfo
                          var newsSource: String = "",

                          @ColumnInfo
                          var newsUrl: String = "",

                          @ColumnInfo
                          var newsTitle: String = "",

                          @ColumnInfo
                          var collectionTime: Long = 0L)