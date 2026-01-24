package com.waiyan.myittar_oo_emr.data

expect class SettingsStorage {
    fun putString(key: String, value: String)
    fun getString(key: String, defaultValue: String): String
}