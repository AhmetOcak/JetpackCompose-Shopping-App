package com.ahmetocak.shoppingapp.core.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager

abstract class Notifier(private val notificationManager: NotificationManager) {

    abstract val notificationChannelId: String
    abstract val notificationChannelName: String
    abstract val notificationId: Int

    fun launchNotification() {
        val channel = createNotificationChannel()
        val notificationBuilder = buildNotification()

        notificationManager.apply {
            createNotificationChannel(channel)
            notify(notificationId, notificationBuilder)
        }
    }

    open fun createNotificationChannel(
        importance: Int = NotificationManager.IMPORTANCE_HIGH
    ): NotificationChannel {
        return NotificationChannel(
            notificationChannelId,
            notificationChannelName,
            importance
        )
    }

    abstract fun buildNotification(): Notification
}