package com.lyloou.flow

import android.app.Application
import androidx.multidex.MultiDexApplication

class App : MultiDexApplication()  {
    companion object {
        lateinit var instance: Application
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}