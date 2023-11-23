package com.ahmetocak.shoppingapp.core.alarm

import android.app.PendingIntent

interface AlarmScheduler {
    fun createPendingIntent(): PendingIntent

    fun schedule()
}