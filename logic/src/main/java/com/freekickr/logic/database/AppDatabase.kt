package com.freekickr.logic.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.freekickr.logic.database.daos.RecordDao
import com.freekickr.logic.database.entities.RecordItem

@Database(entities = [RecordItem::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract val recordDao: RecordDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(context.applicationContext,
                    AppDatabase::class.java, "records_db")
                        .fallbackToDestructiveMigration()
                        .build()
                INSTANCE = instance
                }
                return instance
            }
        }
    }
}