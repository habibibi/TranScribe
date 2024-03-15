package com.ikp.transcribe.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ikp.transcribe.data.dao.TransactionDao
import com.ikp.transcribe.data.table.Transaction

@Database(entities = [Transaction::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun TransactionDao() : TransactionDao

    companion object{
        private var instance: AppDatabase? = null

        fun getInstance (context: Context) : AppDatabase{
            if (instance==null){
                instance = Room.databaseBuilder(context, AppDatabase::class.java  ,"transaction")
                    .build()
            }
            return instance!!
        }
    }
}