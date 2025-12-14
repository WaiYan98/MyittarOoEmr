package com.waiyan.myittar_oo_emr.local_service

import com.waiyan.myittar_oo_emr.data.PatientWithDetail
import com.waiyan.myittar_oo_emr.data.PatientWithVisitAndFollowUp
import com.waiyan.myittar_oo_emr.data.entity.FollowUp
import com.waiyan.myittar_oo_emr.data.entity.MedicalInfo
import com.waiyan.myittar_oo_emr.data.entity.Patient
import com.waiyan.myittar_oo_emr.data.entity.Visit
import kotlinx.coroutines.flow.Flow

interface EmrRepository {

    suspend fun upsertPatient(patient: Patient): Result<Long>
    suspend fun upsertMedicalInfo(medicalInfo: MedicalInfo): Result<Unit>
    suspend fun upsertVisit(visit: Visit): Result<Unit>
    suspend fun upsertFollowUp(followUp: FollowUp): Result<Unit>

    fun getAllPatient(): Flow<List<Patient>>

    fun getPatientWithDetail(patientId: Long): Flow<PatientWithDetail>

    fun getPatientWithVisitAndFollowUp(): Flow<List<PatientWithVisitAndFollowUp>>

    fun getAllFollowUp(): Flow<List<FollowUp>>

    fun getAllVisit(): Flow<List<Visit>>

    fun getPatientById(patientId: Long): Flow<Patient>


}