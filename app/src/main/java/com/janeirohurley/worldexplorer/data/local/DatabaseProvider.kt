package com.janeirohurley.worldexplorer.data.local

import android.content.Context
import androidx.room.Room

object DatabaseProvider {
    private var database: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return database ?: Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "world_explorer_db"
        ).build().also { database = it }
    }
}
