package com.waiyan.myittar_oo_emr.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MedicalInfo(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val patientId: Long,
    val allergies: String,
    val chronicConditions: String,
    val currentMedication: String
)
