package com.gurkha.hr

import android.app.Application
import com.gurkha.di.androidKoinInit
import com.gurkha.hr.logger.setupLogger

class GurkhaApp : Application() {

    override fun onCreate() {
        super.onCreate()
        androidKoinInit(this@GurkhaApp)
        setupLogger("Android")
    }


}