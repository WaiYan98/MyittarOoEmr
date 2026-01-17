package com.waiyan.myittar_oo_emr.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.waiyan.myittar_oo_emr.local.database.EmrDatabase

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

// This object tells Room how to migrate from version 1 to 2
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // SQL command to add the 'occupation' column to the 'Patient' table
        // We set a default value of '' (an empty string) for all existing rows.
        db.execSQL("ALTER TABLE Patient ADD COLUMN occupation TEXT NOT NULL DEFAULT ''")
    }
}

fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<EmrDatabase> {
    val appContext = context.applicationContext
    val dbFile = appContext.getDatabasePath("emr_database.db")
    return Room.databaseBuilder<EmrDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
    .addMigrations(MIGRATION_1_2) // <-- ADD THIS LINE
    .setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
}