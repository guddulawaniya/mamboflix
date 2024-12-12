package com.mamboflix.utils

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager(private val context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

    fun isFirstLaunch(): Boolean {
        val isFirstLaunch = sharedPreferences.getBoolean("isFirstLaunch", true)
        if (isFirstLaunch) {
            // Mark that the app has been launched
            sharedPreferences.edit().putBoolean("isFirstLaunch", false).apply()
        }
        return isFirstLaunch
    }
}