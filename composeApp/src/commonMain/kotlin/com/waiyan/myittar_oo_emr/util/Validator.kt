package com.waiyan.myittar_oo_emr.util

import com.waiyan.myittar_oo_emr.data.ValidationResult

object Validator {

    fun validatePatientInfo(
        name: String,
        age: String,
        phone: String,
        address: String,
        fee: String
    ): ValidationResult {
        val patientAge =
            age.toIntOrNull() ?: return ValidationResult.Failure("please Input Valid Age")
        val consultationFee =  fee.toLongOrNull() ?: return ValidationResult.Failure("please Input Valid Fee")


        if (name.isBlank()) return ValidationResult.Failure("Please Input Name")
        if (patientAge <= 0) return ValidationResult.Failure("Age Cannot be zero!")
        if (phone.isBlank()) return ValidationResult.Failure("Enter Valid Number")
        if (address.isBlank()) return ValidationResult.Failure("Enter Valid Address!")

        return ValidationResult.Success
    }
}