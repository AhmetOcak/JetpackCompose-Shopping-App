package com.ahmetocak.shoppingapp.common.helpers

import android.content.Context
import android.content.SharedPreferences
import com.ahmetocak.shoppingapp.utils.MY_PREF

class PreferenceManager(context: Context) {

    private val preferences: SharedPreferences =
        context.getSharedPreferences(MY_PREF, Context.MODE_PRIVATE)

    fun saveData(key: String, value: Boolean) {
        preferences.edit().apply {
            putBoolean(key, value)
            apply()
        }
    }

    fun saveData(key: String, value: String) {
        preferences.edit().apply {
            putString(key, value)
            apply()
        }
    }

    fun saveData(key: String, value: Int) {
        preferences.edit().apply {
            putInt(key, value)
            apply()
        }
    }

    fun saveData(key: String, value: Long) {
        preferences.edit().apply {
            putLong(key, value)
            apply()
        }
    }

    fun saveData(key: String, value: Float) {
        preferences.edit().apply {
            putFloat(key, value)
            apply()
        }
    }

    fun getData(key: String, defaultValue: Boolean): Boolean {
        return preferences.getBoolean(key, defaultValue)
    }

    fun getData(key: String, defaultValue: String): String {
        return preferences.getString(key, defaultValue) ?: defaultValue
    }

    fun getData(key: String, defaultValue: Int): Int {
        return preferences.getInt(key, defaultValue)
    }

    fun getData(key: String, defaultValue: Long): Long {
        return preferences.getLong(key, defaultValue)
    }

    fun getData(key: String, defaultValue: Float): Float {
        return preferences.getFloat(key, defaultValue)
    }
}