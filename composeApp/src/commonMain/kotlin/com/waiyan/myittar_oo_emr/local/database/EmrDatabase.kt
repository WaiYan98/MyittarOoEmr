package com.waiyan.myittar_oo_emr.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.waiyan.myittar_oo_emr.data.entity.FollowUp
import com.waiyan.myittar_oo_emr.data.entity.MedicalInfo
import com.waiyan.myittar_oo_emr.data.entity.Patient
import com.waiyan.myittar_oo_emr.data.entity.Visit
import com.waiyan.myittar_oo_emr.local.database.dao.FollowUpDao
import com.waiyan.myittar_oo_emr.local.database.dao.MedicalInfoDao
import com.waiyan.myittar_oo_emr.local.database.dao.PatientDao
import com.waiyan.myittar_oo_emr.local.database.dao.VisitDao

@Database(
    entities = [Patient::class, MedicalInfo::class, FollowUp::class, Visit::class], version = 1
)
abstract class EmrDatabase : RoomDatabase() {

    abstract fun getPatientDao(): PatientDao
    abstract fun getMedicalInfoDao(): MedicalInfoDao
    abstract fun getVisitDao(): VisitDao
    abstract fun getFollowUpDao(): FollowUpDao
}