package com.samkt.weathersavy.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.samkt.weathersavy.MainActivity
import com.samkt.weathersavy.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

private const val CHANNEL_ID = "weather_savy_notification_id"
private const val NOTIFICATION_ID = 1
private const val CHANNEL_NAME = "weather_channel"

private const val TAG = "NotificationHelper"

class NotificationHelper
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
    ) {
        private val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        private val intent =
            Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
        private val pendingIntent: PendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        fun showNotification(
            title: String = "Sample title",
            content: String = "desc",
        ) {
            Log.d(TAG, "Showing notification")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val descriptionText = "Sample description"
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel =
                    NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                        description = descriptionText
                    }
                notificationManager.createNotificationChannel(channel)
            }
            val builder =
                NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_clouds)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            notificationManager.notify(NOTIFICATION_ID, builder.build())
        }
    }
