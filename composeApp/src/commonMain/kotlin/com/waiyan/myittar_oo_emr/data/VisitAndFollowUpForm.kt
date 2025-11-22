package com.waiyan.myittar_oo_emr.data

import com.waiyan.myittar_oo_emr.data.entity.FollowUp
import com.waiyan.myittar_oo_emr.data.entity.Visit
import com.waiyan.myittar_oo_emr.util.LocalTime

data class VisitAndFollowUpForm(
    val patientId: Long,
    val diagnosis: String,
    val prescription: String,
    val fee: String,
    val followUpDate: Long,
    val reasonForFollowUp: String
)

fun VisitAndFollowUpForm.toVisit(): Visit {
    return Visit(
        patientId = this.patientId,
        diagnosis = this.diagnosis,
        prescription = this.prescription,
        fee = this.fee.toLong(),
        date = LocalTime.getCurrentTimeMillis()
    )
}

fun VisitAndFollowUpForm.toFollowUp(): FollowUp {
    return FollowUp(
        patientId = this.patientId,
        reasonForFollowUp = this.reasonForFollowUp,
        date = followUpDate,

        )
}
