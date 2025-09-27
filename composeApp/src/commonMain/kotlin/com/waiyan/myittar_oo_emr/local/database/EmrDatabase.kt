package com.waiyan.myittar_oo_emr.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.waiyan.myittar_oo_emr.data.Patient

@Database(entities = [Patient::class], version = 1)
abstract class EmrDatabase : RoomDatabase() {

    abstract fun getDao(): PatientDao
}