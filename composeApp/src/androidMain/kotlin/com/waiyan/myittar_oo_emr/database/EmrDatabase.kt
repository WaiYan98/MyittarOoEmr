package com.waiyan.myittar_oo_emr.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.waiyan.myittar_oo_emr.local.database.EmrDatabase

fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<EmrDatabase> {
    val appContext = context.applicationContext
    val dbFile = appContext.getDatabasePath("emr_database.db")
    return Room.databaseBuilder<EmrDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}