package com.prokoshevnik.top10sample.backend.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.prokoshevnik.top10sample.backend.db.dao.GoogleSearchDao
import com.prokoshevnik.top10sample.backend.db.entity.GoogleSearchItem
import com.prokoshevnik.top10sample.backend.db.entity.GoogleSearchResult

@Database(
    entities = [GoogleSearchItem::class, GoogleSearchResult::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun googleSearchDao(): GoogleSearchDao

    companion object {
        private const val FILE_NAME = "database"
        private var instance: AppDatabase? = null

        fun getInstance(context: Context) = instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(context, AppDatabase::class.java, FILE_NAME).build()
        }
    }
}
