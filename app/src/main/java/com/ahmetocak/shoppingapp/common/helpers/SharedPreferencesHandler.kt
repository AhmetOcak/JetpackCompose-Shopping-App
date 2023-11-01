package com.ahmetocak.shoppingapp.common.helpers

import android.content.SharedPreferences
import com.ahmetocak.shoppingapp.utils.USER_REMEMBER_ME_KEY

fun SharedPreferences.Editor.rememberMe() {
    putBoolean(USER_REMEMBER_ME_KEY, true)
    apply()
}

fun SharedPreferences.Editor.removeRememberMe() {
    putBoolean(USER_REMEMBER_ME_KEY, false)
    apply()
}

fun SharedPreferences.getRememberMe() : Boolean {
    return getBoolean(USER_REMEMBER_ME_KEY, false)
}