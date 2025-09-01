package com.gurkha.hr.components

import android.content.Context
import android.widget.Toast
import org.koin.mp.KoinPlatform.getKoin

actual class PlatformMessage() {
    actual fun showToast(message: String) {
        val context = getKoin().get<Context>()
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}