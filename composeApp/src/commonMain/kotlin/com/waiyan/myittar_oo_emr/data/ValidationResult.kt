package com.waiyan.myittar_oo_emr.data

sealed class ValidationResult {
    object Success : ValidationResult()
    data class Failure(val message: String) : ValidationResult()
}