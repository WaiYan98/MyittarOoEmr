package com.waiyan.myittar_oo_emr.util

import com.waiyan.myittar_oo_emr.data.ValidationResult
import kotlin.time.Clock

object Validator {

    fun validatePatientInfo(
        name: String,
        age: String,
        phone: String,
        address: String
    ): ValidationResult {
        if (name.isBlank()) return ValidationResult.Failure("Please Input Name")
        val patientAge =
            age.toIntOrNull() ?: return ValidationResult.Failure("please Input Valid Age")
        if (patientAge <= 0) return ValidationResult.Failure("Age Cannot be zero!")
        if (phone.isBlank()) return ValidationResult.Failure("Enter Valid Number")
        if (address.isBlank()) return ValidationResult.Failure("Enter Valid Address!")

        return ValidationResult.Success
    }

    fun validateVisitAndFollowUp(
        diagnosis: String,
        prescription: String,
        fee: String,
        followUpDate: Long,
        reasonForFollowUp: String,
        isCheckedFollowUP: Boolean
    ): ValidationResult {
        if (diagnosis.isBlank()) return ValidationResult.Failure("Please Input Diagnosis")
        if (prescription.isBlank()) return ValidationResult.Failure("Please Input Prescription")
        val consultationFee =
            fee.toLongOrNull() ?: return ValidationResult.Failure("please Input Valid Fee")

        if (isCheckedFollowUP) {
            if (followUpDate == 0L) return ValidationResult.Failure("Please Input Follow Up Date")
            if (reasonForFollowUp.isBlank()) return ValidationResult.Failure("Please Input Reason For Follow Up")
        }
        return ValidationResult.Success
    }

}