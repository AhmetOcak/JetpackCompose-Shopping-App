package com.ahmetocak.shoppingapp.core.notification

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import com.ahmetocak.shoppingapp.presentation.MainActivity
import com.ahmetocak.shoppingapp.R
import com.ahmetocak.shoppingapp.utils.NotificationChannelIds
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DailyShoppingNotifier @Inject constructor(
    @ApplicationContext private val context: Context,
    notificationManager: NotificationManager,
) : Notifier(notificationManager) {

    override val notificationChannelId: String
        get() = NotificationChannelIds.SHOPPING_NOTIFICATION

    override val notificationChannelName: String
        get() = context.getString(R.string.shopping_channel_name)
    override val notificationId: Int
        get() = 1

    override fun buildNotification(): Notification {
        val resultIntent = Intent(context, MainActivity::class.java)

        val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(resultIntent)

            getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        }

        return NotificationCompat.Builder(context, NotificationChannelIds.SHOPPING_NOTIFICATION)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(context.getString(R.string.shopping_notification_title))
            .setContentText(context.getString(R.string.shop_notification_description))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_RECOMMENDATION)
            .setContentIntent(resultPendingIntent)
            .setAutoCancel(true)
            .build()
    }
}