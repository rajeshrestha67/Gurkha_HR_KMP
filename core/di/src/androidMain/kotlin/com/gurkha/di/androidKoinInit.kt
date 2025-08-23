package com.gurkha.di

import android.content.Context
import org.koin.android.ext.koin.androidContext


fun androidKoinInit(context: Context){
    initKoin {
        androidContext(context)
    }
}