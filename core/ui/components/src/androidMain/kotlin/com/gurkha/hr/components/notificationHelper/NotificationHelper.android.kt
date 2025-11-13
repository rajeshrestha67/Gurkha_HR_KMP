package com.gurkha.hr.components.notificationHelper

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import org.koin.mp.KoinPlatform.getKoin

actual object NotificationHelper {
    actual fun showNetworkNotification(message: String) {
        val context: Context = getKoin().get()
        val manager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "CHANNEL_ID",
                "Network Logs",
                NotificationManager.IMPORTANCE_LOW
            )

            manager.createNotificationChannel(channel)
        }
        val notification = NotificationCompat.Builder(context, "CHANNEL_ID")
            .setContentTitle("Network Log")
            .setContentText(message)
            .setSmallIcon(android.R.drawable.ic_menu_info_details)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
        manager.notify(message.hashCode(), notification)
    }
}