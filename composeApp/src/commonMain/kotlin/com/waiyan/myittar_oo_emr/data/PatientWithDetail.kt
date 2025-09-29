package com.waiyan.myittar_oo_emr.data

import androidx.room.Embedded
import androidx.room.Relation
import com.waiyan.myittar_oo_emr.data.entity.MedicalInfo
import com.waiyan.myittar_oo_emr.data.entity.Patient
import com.waiyan.myittar_oo_emr.data.entity.Visit

data class PatientWithDetail(
    @Embedded
    val patient: Patient,
    @Relation(
        parentColumn = "id",
        entityColumn = "patientId"
    )
    val medicalInfo: MedicalInfo,

    @Relation(
        parentColumn = "id",
        entityColumn = "patientId"
    )
    val visits: List<Visit>
)