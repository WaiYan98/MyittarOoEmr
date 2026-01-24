package com.waiyan.myittar_oo_emr.data

import android.content.Context

actual class SettingsStorage(private val context: Context) {
    private val sharedPreferences = context.getSharedPreferences("pin_prefs", Context.MODE_PRIVATE)

    actual fun putString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    actual fun getString(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }
}
