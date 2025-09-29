package com.waiyan.myittar_oo_emr.local_service

import com.waiyan.myittar_oo_emr.data.PatientWithDetail
import com.waiyan.myittar_oo_emr.data.PatientWithVisitAndFollowUp
import com.waiyan.myittar_oo_emr.data.entity.FollowUp
import com.waiyan.myittar_oo_emr.data.entity.MedicalInfo
import com.waiyan.myittar_oo_emr.data.entity.Patient
import com.waiyan.myittar_oo_emr.data.entity.Visit
import kotlinx.coroutines.flow.Flow

interface EmrRepository {

    suspend fun insertPatient(patient: Patient)
    suspend fun insertMedicalInfo(medicalInfo: MedicalInfo)
    suspend fun insertVisit(visit: Visit)
    suspend fun insertFollowUp(followUp: FollowUp)

    fun getAllPatient(): Flow<List<Patient>>

    fun getPatientWithDetail(patientId: Long): Flow<PatientWithDetail>

    fun getPatientWithVisitAndFollowUp(): Flow<List<PatientWithVisitAndFollowUp>>
}