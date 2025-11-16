package com.waiyan.myittar_oo_emr.data

import com.waiyan.myittar_oo_emr.screen.component.patient_form_screen.Gender

data class PatientForm(
    val name: String,
    val age: String,
    val gender: Gender,
    val phone: String,
    val address: String,
    val allergies: String,
    val chronicConditions: String,
    val currentMedication: String,
    val prescription: String,
    val fee: String,
    val diagnosis: String,
    val followUpDate: Long,
    val reasonForFollowUp: String
)
