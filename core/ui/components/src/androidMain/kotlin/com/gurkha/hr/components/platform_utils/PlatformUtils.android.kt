package com.gurkha.hr.components.platform_utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.net.toUri
import org.koin.mp.KoinPlatform.getKoin


actual class PlatformUtils {
    actual fun copyToClipboard(text: String) {
        val context: Context = getKoin().get()
        val clipboard = getSystemService(context, ClipboardManager::class.java)
        val clip = ClipData.newPlainText("Copied Text", text)
        clipboard?.setPrimaryClip(clip)
    }

    actual fun shareText(text: String, title: String?) {
        val context: Context = getKoin().get()
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, title).apply {
            // Needed if called outside of an Activity context
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(shareIntent)
    }

    actual fun callPhoneNumber(phoneNumber: String) {
        val context: Context = getKoin().get()
        val intent = Intent(Intent.ACTION_DIAL).apply {
            // Use 'tel:' URI scheme to indicate a phone number
            data = "tel:$phoneNumber".toUri()
            // Needed if called outside of an Activity context
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }
}