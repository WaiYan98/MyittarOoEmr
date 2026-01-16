package com.waiyan.myittar_oo_emr.data

import com.waiyan.myittar_oo_emr.data.entity.FollowUp
import com.waiyan.myittar_oo_emr.data.entity.MedicalInfo
import com.waiyan.myittar_oo_emr.data.entity.Patient
import com.waiyan.myittar_oo_emr.data.entity.Visit
import com.waiyan.myittar_oo_emr.screen.component.patient_form_screen.Gender
import com.waiyan.myittar_oo_emr.util.LocalTime

data class PatientForm(
    val name: String,
    val age: Int,
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

fun PatientForm.patientFormToPatient(): Patient {
    return Patient(
        name = this.name,
        age = this.age,
        gender = this.gender.name,
        phone = this.phone,
        address = this.address
    )
}

fun PatientForm.patientFormToMedicalInfo(patientId: Long): MedicalInfo {
    return MedicalInfo(
        patientId = patientId,
        allergies = this.allergies,
        chronicConditions = this.chronicConditions,
        currentMedication = this.currentMedication
    )
}

fun PatientForm.patientFormToVisit(patientId: Long): Visit {
    return Visit(
        patientId = patientId,
        date = LocalTime.getCurrentTimeMillis(),
        diagnosis = this.diagnosis,
        prescription = this.prescription,
        fee = this.fee.toLongOrNull() ?: 0L
    )
}

fun PatientForm.patientFormToFollowUp(patientId: Long): FollowUp {
    return FollowUp(
        patientId = patientId,
        date = this.followUpDate,
        reasonForFollowUp = this.reasonForFollowUp
    )
}