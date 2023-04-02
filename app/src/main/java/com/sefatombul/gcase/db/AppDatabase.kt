package com.sefatombul.gcase.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sefatombul.gcase.data.local.RecentSearchDao
import com.sefatombul.gcase.data.local.RecentSearchModel

@Database(entities = [RecentSearchModel::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getRecentSearchDao(): RecentSearchDao
}