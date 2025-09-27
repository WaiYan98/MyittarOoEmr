package com.waiyan.myittar_oo_emr.local_service

import com.waiyan.myittar_oo_emr.data.Patient
import kotlinx.coroutines.flow.Flow

interface PatientRepository {

    suspend fun insertPatient(patient: Patient)

     fun getAllPatient(): Flow<List<Patient>>
}