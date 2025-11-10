package com.gurkha.hr

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.RemoteInput

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notifId = intent.getIntExtra("notifId", 0)
        when (intent.action) {

            ACTION_REPLY -> {
                val replyText = RemoteInput.getResultsFromIntent(intent)
                    ?.getCharSequence(REMOTE_INPUT_KEY)?.toString()

                if (!replyText.isNullOrEmpty()) {
                    // TODO: send message via API/socket
                    Log.d("Notification", "User replied: $replyText")
                }

                NotificationManagerCompat.from(context).cancel(notifId)
            }

            ACTION_MARK_READ -> {
                // TODO: update server read status
                Log.d("Notification", "Marked as read")
                NotificationManagerCompat.from(context).cancel(notifId)
            }
        }
    }
}
