package com.gurkha.hr.dashboard

import android.app.Activity
import org.koin.mp.KoinPlatform.getKoin

actual fun exitApp() {
    val context: Activity? = getKoin().getProperty("activity")
    context?.finishAffinity()
}