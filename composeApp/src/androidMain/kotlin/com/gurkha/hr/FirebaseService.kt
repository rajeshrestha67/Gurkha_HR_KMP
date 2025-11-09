package com.gurkha.hr

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.graphics.toColorInt
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.gurkha.hr.datastore.token.model.Token
import com.gurkha.hr.datastore.token.repository.TokenRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import java.net.HttpURLConnection
import java.net.URL

class FirebaseService : FirebaseMessagingService() {
    val repository: TokenRepository by inject()
    val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        remoteMessage.notification?.let { notification ->
            CoroutineScope(Dispatchers.IO).launch {
                showNotification(
                    context = this@FirebaseService,
                    title = notification.title ?: "",
                    body = notification.body ?: "",
                    notifyId = 1,
                    imageUrl = notification.imageUrl?.toString(),
                    pendingIntent = PendingIntent.getActivity(
                        this@FirebaseService,
                        1,
                        Intent(),
                        FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )
                )
            }

        }

    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        scope.launch {
            val tokenData = repository.token.firstOrNull() ?: Token()
            repository.saveToken(tokenData.copy(fcmToken = token))
        }
        println("firebase newToken: $token")
    }

    suspend fun loadBitmapFromUrl(url: String): Bitmap? = withContext(Dispatchers.IO) {
        try {
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private suspend fun showNotification(
        context: Context,
        title: String,
        body: String,
        imageUrl: String?,
        notifyId: Int,
        pendingIntent: PendingIntent
    ) {

        // Download image in background
        val bitmap = imageUrl?.let { loadBitmapFromUrl(it) }

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.splash_icon)
            .setContentTitle(title)
            .setContentText(body)
            .setNumber(3)
            .setDefaults(NotificationCompat.DEFAULT_SOUND or NotificationCompat.DEFAULT_VIBRATE)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setColor("#FF4B662C".toColorInt())
            .setAutoCancel(true)

        // 👇 Apply BigPictureStyle if bitmap is available
        if (bitmap != null) {
            builder.setLargeIcon(bitmap)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                builder.setStyle(
                    NotificationCompat.BigPictureStyle()
                        .bigPicture(bitmap)
                        .bigLargeIcon(null as Bitmap?)
                        .setBigContentTitle(title)
                        .setSummaryText(body)
                        .showBigPictureWhenCollapsed(false)

                )
                    // Force heads-up/expanded behavior
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
            } // ensure heads-up
        }

        // Create the NotificationChannel for Android 8+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = title
            val descriptionText = "Biometric Channel for ${context.getString(R.string.app_name)}"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

        // Finally show notification
        with(NotificationManagerCompat.from(context)) {
            if (areNotificationsEnabled()) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    notify(notifyId, builder.build())
                }
            }
        }
    }

    companion object {
        private const val CHANNEL_ID = "Normal Biometric"
    }

}