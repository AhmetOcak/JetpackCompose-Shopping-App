package com.ahmetocak.shoppingapp.core.alarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import com.ahmetocak.shoppingapp.MainActivity
import com.ahmetocak.shoppingapp.R
import com.ahmetocak.shoppingapp.utils.NotificationChannelIds

class ShoppingAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, p1: Intent?) {
        if (context != null) {
            val resultIntent = Intent(context, MainActivity::class.java)

            val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(context).run {
                addNextIntentWithParentStack(resultIntent)

                getPendingIntent(0,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            }

            val notificationBuilder =
                NotificationCompat.Builder(context, NotificationChannelIds.SHOPPING_NOTIFICATION)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(context.getString(R.string.shopping_notification_title))
                    .setContentText(context.getString(R.string.shop_notification_description))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setCategory(NotificationCompat.CATEGORY_RECOMMENDATION)
                    .setContentIntent(resultPendingIntent)

            val channel = NotificationChannel(
                NotificationChannelIds.SHOPPING_NOTIFICATION,
                context.getString(R.string.shopping_channel_name),
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = context.getString(R.string.shopping_channel_description)
            }

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.apply {
                createNotificationChannel(channel)
                notify(111, notificationBuilder.build())
            }
        }
    }
}