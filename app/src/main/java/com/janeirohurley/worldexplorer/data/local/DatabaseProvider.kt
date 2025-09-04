package com.janeirohurley.worldexplorer.data.local

import android.content.Context
import androidx.room.Room

object DatabaseProvider {
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "worldexplorer.db"
            )
                .fallbackToDestructiveMigration() // 💣 supprime tout et recrée
                .build()
        }
        return INSTANCE!!
    }
}
