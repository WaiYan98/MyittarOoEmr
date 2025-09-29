package com.waiyan.myittar_oo_emr.data

import androidx.room.Embedded
import androidx.room.Relation
import com.waiyan.myittar_oo_emr.data.entity.FollowUp
import com.waiyan.myittar_oo_emr.data.entity.Patient
import com.waiyan.myittar_oo_emr.data.entity.Visit

data class PatientWithVisitAndFollowUp(
    @Embedded
    val patient: Patient,
    @Relation(
        parentColumn = "id",
        entityColumn = "patientId"
    )
    val visits: List<Visit>,
    @Relation(
        parentColumn = "id",
        entityColumn = "patientId"
    )
    val followUp: List<FollowUp>
)
