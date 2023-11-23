package com.ahmetocak.shoppingapp.core.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.ahmetocak.shoppingapp.utils.ShoppingDailyNotificationTimeManager
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Calendar
import javax.inject.Inject

class ShoppingAlarmManager @Inject constructor(@ApplicationContext context: Context) {
    private val calendar: Calendar = Calendar.getInstance().apply {
        timeInMillis = System.currentTimeMillis()
        set(Calendar.HOUR_OF_DAY, ShoppingDailyNotificationTimeManager.REMINDER_HOUR)
        set(Calendar.MINUTE, ShoppingDailyNotificationTimeManager.REMINDER_MINUTES)
    }

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    private val pendingIntent: PendingIntent = PendingIntent.getBroadcast(
        context,
        0,
        Intent(context, ShoppingAlarmReceiver::class.java),
        PendingIntent.FLAG_IMMUTABLE
    )

    fun initShoppingNotificationAlarm() {
        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1)
        }

        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }
}