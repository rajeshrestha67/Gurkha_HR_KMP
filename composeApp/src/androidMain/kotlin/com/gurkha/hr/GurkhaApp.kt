package com.gurkha.hr

import android.app.Application
import com.gurkha.di.androidKoinInit

class GurkhaApp: Application() {

    override fun onCreate() {
        super.onCreate()
        androidKoinInit(this@GurkhaApp)
    }
}