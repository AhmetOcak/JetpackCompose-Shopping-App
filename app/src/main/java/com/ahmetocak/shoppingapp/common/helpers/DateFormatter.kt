package com.ahmetocak.shoppingapp.common.helpers

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun Long.formatDate(): String? {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val instant = Instant.ofEpochMilli(this)
    val date = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
    return formatter.format(date)
}