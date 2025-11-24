package com.waiyan.myittar_oo_emr.data

data class UpcomingFollowUp(
    val followUpDate: String,
    val patientName: String,
    val reasonForFollowUp: String,
    val timeUntil: String
)
