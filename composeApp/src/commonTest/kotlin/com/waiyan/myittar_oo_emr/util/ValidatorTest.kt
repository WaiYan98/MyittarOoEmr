package com.waiyan.myittar_oo_emr.util

import com.waiyan.myittar_oo_emr.data.ValidationResult
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ValidatorTest {

    // --- Tests for validatePatientInfo ---

    @Test
    fun `validatePatientInfo with valid data returns Success`() {
        val result = Validator.validatePatientInfo("Name", "30", "1234567890", "Address")
        assertTrue(result is ValidationResult.Success)
    }

    @Test
    fun `validatePatientInfo with empty name returns Failure`() {
        val result = Validator.validatePatientInfo("", "30", "1234567890", "Address")
        assertTrue(result is ValidationResult.Failure)
        assertEquals("Please Input Name", result.message)
    }

    @Test
    fun `validatePatientInfo with invalid age returns Failure`() {
        val result = Validator.validatePatientInfo("Name", "abc", "1234567890", "Address")
        assertTrue(result is ValidationResult.Failure)
        assertEquals("please Input Valid Age", result.message)
    }

    @Test
    fun `validatePatientInfo with zero age returns Failure`() {
        val result = Validator.validatePatientInfo("Name", "0", "1234567890", "Address")
        assertTrue(result is ValidationResult.Failure)
        assertEquals("Age Cannot be zero!", result.message)
    }

    @Test
    fun `validatePatientInfo with negative age returns Failure`() {
        val result = Validator.validatePatientInfo("Name", "-5", "1234567890", "Address")
        assertTrue(result is ValidationResult.Failure)
        assertEquals("Age Cannot be zero!", result.message)
    }

    @Test
    fun `validatePatientInfo with empty phone returns Failure`() {
        val result = Validator.validatePatientInfo("Name", "30", "", "Address")
        assertTrue(result is ValidationResult.Failure)
        assertEquals("Enter Valid Number", result.message)
    }

    @Test
    fun `validatePatientInfo with empty address returns Failure`() {
        val result = Validator.validatePatientInfo("Name", "30", "1234567890", "")
        assertTrue(result is ValidationResult.Failure)
        assertEquals("Enter Valid Address!", result.message)
    }

    // --- Tests for validateVisitAndFollowUp ---

    @Test
    fun `validateVisitAndFollowUp with valid data without follow-up returns Success`() {
        val result = Validator.validateVisitAndFollowUp("Diagnosis", "Prescription", "100", 0L, "", false)
        assertTrue(result is ValidationResult.Success)
    }

    @Test
    fun `validateVisitAndFollowUp with valid data with follow-up returns Success`() {
        val result = Validator.validateVisitAndFollowUp("Diagnosis", "Prescription", "100", 123L, "Reason", true)
        assertTrue(result is ValidationResult.Success)
    }

    @Test
    fun `validateVisitAndFollowUp with empty diagnosis returns Failure`() {
        val result = Validator.validateVisitAndFollowUp("", "Prescription", "100", 0L, "", false)
        assertTrue(result is ValidationResult.Failure)
        assertEquals("Please Input Diagnosis", result.message)
    }

    @Test
    fun `validateVisitAndFollowUp with empty prescription returns Failure`() {
        val result = Validator.validateVisitAndFollowUp("Diagnosis", "", "100", 0L, "", false)
        assertTrue(result is ValidationResult.Failure)
        assertEquals("Please Input Prescription", result.message)
    }

    @Test
    fun `validateVisitAndFollowUp with invalid fee returns Failure`() {
        val result = Validator.validateVisitAndFollowUp("Diagnosis", "Prescription", "abc", 0L, "", false)
        assertTrue(result is ValidationResult.Failure)
        assertEquals("please Input Valid Fee", result.message)
    }

    @Test
    fun `validateVisitAndFollowUp with follow-up checked but no date returns Failure`() {
        val result = Validator.validateVisitAndFollowUp("Diagnosis", "Prescription", "100", 0L, "Reason", true)
        assertTrue(result is ValidationResult.Failure)
        assertEquals("Please Input Follow Up Date", result.message)
    }

    @Test
    fun `validateVisitAndFollowUp with follow-up checked but empty reason returns Failure`() {
        val result = Validator.validateVisitAndFollowUp("Diagnosis", "Prescription", "100", 123L, "", true)
        assertTrue(result is ValidationResult.Failure)
        assertEquals("Please Input Reason For Follow Up", result.message)
    }
}