package com.janeirohurley.worldexplorer.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [CountryEntity::class, Comment::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun countryDao(): CountryDao
    abstract fun commentDao(): CommentDao
}
