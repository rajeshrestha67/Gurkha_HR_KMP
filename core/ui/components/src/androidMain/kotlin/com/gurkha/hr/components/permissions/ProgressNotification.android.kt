package com.gurkha.hr.components.permissions

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.mp.KoinPlatform.getKoin
import java.io.InputStream

actual class ProgressNotification {
    var bitmap: Bitmap? = null


    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    actual suspend fun showNotification(progress: Int) {
        val context: Context = getKoin().get()

        val channelId = "upload_channel"
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            nm.createNotificationChannel(
                NotificationChannel(
                    channelId,
                    "Upload Progress",
                    NotificationManager.IMPORTANCE_LOW
                )
            )
        }

        val isCompleted = progress == 100
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(
                if (isCompleted) android.R.drawable.stat_sys_upload_done
                else android.R.drawable.stat_sys_upload
            )
            .setContentTitle(if (!isCompleted) "Uploading Image" else "Image Uploaded")
            .setContentText(if (!isCompleted) "$progress% completed" else "Upload complete")
            .setProgress(100, progress, false)
            .setOngoing(progress < 100)

        if (bitmap != null && progress == 100) {
            builder.setLargeIcon(bitmap)
        }
        NotificationManagerCompat.from(context).notify(100, builder.build())
    }

    private suspend fun loadScaledBitmap(uriOrPath: String, maxSize: Int = 256): Bitmap? {
        val context: Context = getKoin().get()
        return withContext(Dispatchers.IO) {
            try {
                val resolver = context.contentResolver
                resolver.openInputStream(uriOrPath.toUri())?.use { stream ->
                    // First decode only bounds
                    val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
                    BitmapFactory.decodeStream(stream, null, options)

                    // Reset stream
                    stream.close()
                    val input: InputStream =
                        resolver.openInputStream(uriOrPath.toUri()) ?: return@use null

                    // Calculate scaling
                    val (width, height) = options.outWidth to options.outHeight
                    var scale = 1
                    while (width / scale > maxSize || height / scale > maxSize) {
                        scale *= 2
                    }
                    val decodeOptions = BitmapFactory.Options().apply { inSampleSize = scale }
                    BitmapFactory.decodeStream(input, null, decodeOptions)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    actual suspend fun preloadImage(uriOrPath: String) {
        bitmap = loadScaledBitmap(uriOrPath)
    }
}