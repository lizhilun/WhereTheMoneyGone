package com.lizl.wtmg.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.blankj.utilcode.util.Utils
import com.lizl.wtmg.db.dao.CreditAccountDao
import com.lizl.wtmg.db.dao.ExpenditureDao
import com.lizl.wtmg.db.dao.CapitalAccountDao
import com.lizl.wtmg.db.model.CreditAccountModel
import com.lizl.wtmg.db.model.ExpenditureModel
import com.lizl.wtmg.db.model.IncomeModel
import com.lizl.wtmg.db.model.CapitalAccountModel

@Database(entities = [ExpenditureModel::class, IncomeModel::class, CapitalAccountModel::class, CreditAccountModel::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase()
{
    private object Singleton
    {
        val singleton: AppDatabase = Room.databaseBuilder(Utils.getApp(), AppDatabase::class.java, "Money.db").allowMainThreadQueries().build()
    }

    companion object
    {
        fun getInstance() = Singleton.singleton
    }

    abstract fun getExpenditureDao(): ExpenditureDao

    abstract fun getPropertyAccountDao(): CapitalAccountDao

    abstract fun getCreditAccountDao(): CreditAccountDao
}