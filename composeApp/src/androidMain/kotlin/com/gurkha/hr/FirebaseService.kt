package com.gurkha.hr

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.Person
import androidx.core.app.RemoteInput
import androidx.core.graphics.toColorInt
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.gurkha.hr.datastore.token.model.Token
import com.gurkha.hr.datastore.token.repository.TokenRepository
import com.gurkha.hr.logger.AppLogger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import java.net.HttpURLConnection
import java.net.URL
import kotlin.time.Clock
import kotlin.time.ExperimentalTime


class FirebaseService : FirebaseMessagingService() {
    val repository: TokenRepository by inject()
    val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    val messages = mutableListOf<NotificationItem>()

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        AppLogger.i(
            tag = TAG,
            message = "Push notification received, title: ${remoteMessage.notification?.title}, body: ${remoteMessage.notification?.body}, data ${remoteMessage.data}"
        )

        val action: String? = remoteMessage.data["action"] ?: "attendanceRejected"
        val type: String? = remoteMessage.data["type"] ?: "attendance"
        val channelID = when (type) {
            "leave" -> LEAVE_CHANNEL_ID
            else -> ATTENDANCE_CHANNEL_ID
        }
        val channelName = when (type) {
            "leave" -> LEAVE_CHANNEL_NAME
            else -> ATTENDANCE_CHANNEL_NAME
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelID,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
            val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nm.createNotificationChannel(channel)
        }
        createNotification(
            channelID = channelID,
            title = remoteMessage.notification?.title,
            message = remoteMessage.notification?.body,
            action = action,
            type = type
        )
//        remoteMessage.notification?.let { notification ->
//            CoroutineScope(Dispatchers.IO).launch {
//                showNotification(
//                    context = this@FirebaseService,
//                    title = notification.title ?: "",
//                    body = notification.body ?: "",
//                    notifyId = 1,
//                    imageUrl = notification.imageUrl?.toString(),
//                    pendingIntent = PendingIntent.getActivity(
//                        this@FirebaseService,
//                        1,
//                        Intent(),
//                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//                    )
//                )
//            }
//        }
//        remoteMessage.notification?.let { notification ->
//            messages.add(
//                generateItem(
//                    (notification.title ?: "").contains("123"),
//                    (notification.title ?: ""),
//                    (notification.body ?: "")
//                )
//            )
//            createChatNotificationChannel(this)
//            showChatNotification(
//                context = this,
//                notifId = 1111,
//                messages = messages,
//                userName = "Chirag Dangol",
//                isMe = (notification.title ?: "").contains("123")
//            )
//        }
    }


    @OptIn(ExperimentalTime::class)
    fun createNotification(
        channelID: String,
        title: String?,
        message: String?,
        action: String?,
        type: String?
    ) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(ACTION, action)
            putExtra(TYPE, type)
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, channelID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        NotificationManagerCompat.from(this)
            .notify(Clock.System.now().nanosecondsOfSecond, notification)
    }

    fun generateItem(
        isYou: Boolean = false,
        title: String,
        message: String
    ): NotificationItem {
        return NotificationItem(
            sender = title,
            message = message,
            person = buildPerson(title, isYou = isYou),
            isMe = title.contains("123")
        )
    }

    fun buildPerson(
        name: String,
        avatarUrl: String? = null,
        isYou: Boolean = false
    ): Person {

        val personBuilder = Person.Builder()
            .setName(name)
            .setImportant(true) // gives better visibility

        if (isYou) {
            personBuilder.setKey("me")
        } else {
            personBuilder.setKey(name)
        }

        avatarUrl?.let {
            //val icon = IconCompat.createWithBitmap(loadBitmapFromUrl(it))
            //personBuilder.setIcon(icon)
        }

        return personBuilder.build()
    }

    fun showChatNotification(
        context: Context,
        notifId: Int,
        isMe: Boolean,
        messages: List<NotificationItem>, // Pair(username, message)
        userName: String
    ) {
        val remoteInput = RemoteInput.Builder(REMOTE_INPUT_KEY)
            .setLabel("Reply...")
            .setAllowFreeFormInput(true)
            .build()

        // PendingIntent for reply
        val replyIntent = Intent(context, NotificationReceiver::class.java).apply {
            action = ACTION_REPLY
            putExtra("notifId", notifId)
        }
        val replyPendingIntent = PendingIntent.getBroadcast(
            context,
            notifId,
            replyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )

        val replyAction = NotificationCompat.Action.Builder(
            android.R.drawable.ic_menu_send,
            "Reply",
            replyPendingIntent
        ).addRemoteInput(remoteInput).build()

        // Mark as read action
        val markReadIntent = Intent(context, NotificationReceiver::class.java).apply {
            action = ACTION_MARK_READ
            putExtra("notifId", notifId)
        }
        val markReadPending = PendingIntent.getBroadcast(
            context,
            notifId + 1,
            markReadIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )


        val messagingStyle = NotificationCompat.MessagingStyle("")
        messages.forEach { item ->
            messagingStyle.addMessage(item.message, System.currentTimeMillis(), item.person)
        }

        val notification = NotificationCompat.Builder(context, CHAT_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.stat_notify_chat)
            .setStyle(messagingStyle)
            .setContentTitle("Message from $userName")
            .setContentText(messages.last().message)
            .addAction(replyAction)
            .addAction(
                NotificationCompat.Action.Builder(
                    android.R.drawable.ic_menu_send, "Mark as read", markReadPending
                ).build()
            )
            .setAutoCancel(true)
            .build()

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        NotificationManagerCompat.from(context).notify(notifId, notification)
    }

    fun createChatNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHAT_CHANNEL_ID,
                CHAT_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nm.createNotificationChannel(channel)
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

        private const val TAG = "FirebaseService"
        private const val LEAVE_CHANNEL_ID = "leave_channel"
        private const val ATTENDANCE_CHANNEL_ID = "attendance_channel"
        const val LEAVE_CHANNEL_NAME = "Leave Notifications"
        const val ATTENDANCE_CHANNEL_NAME = "Attendance Notifications"

        const val CHAT_CHANNEL_ID = "chat_channel"
        const val CHAT_CHANNEL_NAME = "Chat Messages"
        const val ACTION_REPLY = "chat_reply"
        const val ACTION_MARK_READ = "chat_mark_read"
        const val REMOTE_INPUT_KEY = "chat_message_input"

        const val TYPE = "type"
        const val ACTION = "action"

    }


}

data class NotificationItem(
    val sender: String,
    val message: String,
    val person: Person,
    val isMe: Boolean
)