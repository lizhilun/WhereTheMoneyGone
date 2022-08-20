package com.lizl.wtmg.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.blankj.utilcode.util.Utils
import com.lizl.wtmg.db.dao.MoneyTracesDao
import com.lizl.wtmg.db.dao.AccountDao
import com.lizl.wtmg.db.model.MoneyTracesModel
import com.lizl.wtmg.db.model.AccountModel

@Database(entities = [MoneyTracesModel::class, AccountModel::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    private object Singleton {
        val singleton: AppDatabase = Room.databaseBuilder(Utils.getApp(), AppDatabase::class.java, "Money.db").allowMainThreadQueries().build()
    }

    companion object {
        fun getInstance() = Singleton.singleton
    }

    abstract fun getMoneyTracesDao(): MoneyTracesDao

    abstract fun getAccountDao(): AccountDao
}