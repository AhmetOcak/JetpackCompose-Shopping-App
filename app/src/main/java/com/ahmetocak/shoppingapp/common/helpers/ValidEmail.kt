package com.ahmetocak.shoppingapp.common.helpers

import android.util.Patterns.EMAIL_ADDRESS

object ValidEmail {

    fun verifyEmailType(email: String): Boolean = EMAIL_ADDRESS.matcher(email).matches()
}