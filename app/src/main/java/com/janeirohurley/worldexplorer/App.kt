package com.janeirohurley.worldexplorer

import android.app.Application
import com.janeirohurley.worldexplorer.data.local.AppDatabase

class App : Application() {

    companion object {
        lateinit var instance: App
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}
