package com.waiyan.myittar_oo_emr.local_service

import com.waiyan.myittar_oo_emr.data.PatientWithDetail
import com.waiyan.myittar_oo_emr.data.PatientWithVisitAndFollowUp
import com.waiyan.myittar_oo_emr.data.entity.FollowUp
import com.waiyan.myittar_oo_emr.data.entity.MedicalInfo
import com.waiyan.myittar_oo_emr.data.entity.Patient
import com.waiyan.myittar_oo_emr.data.entity.Visit
import com.waiyan.myittar_oo_emr.local.database.dao.FollowUpDao
import com.waiyan.myittar_oo_emr.local.database.dao.MedicalInfoDao
import com.waiyan.myittar_oo_emr.local.database.dao.PatientDao
import com.waiyan.myittar_oo_emr.local.database.dao.VisitDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class EmrRepositoryImpl(
    private val patientDao: PatientDao,
    private val visitDao: VisitDao,
    private val medicalInfoDao: MedicalInfoDao,
    private val followUpDao: FollowUpDao
) : EmrRepository {

    override suspend fun insertPatient(patient: Patient): Result<Long> =
        withContext(Dispatchers.IO) {
            runCatching {
                patientDao.insertPatient(patient)
            }
        }

    override suspend fun insertMedicalInfo(medicalInfo: MedicalInfo): Result<Unit> =
        withContext(Dispatchers.IO) {
            runCatching { medicalInfoDao.insert(medicalInfo) }
        }

    override suspend fun insertVisit(visit: Visit): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching { visitDao.insert(visit) }
    }

    override suspend fun insertFollowUp(followUp: FollowUp): Result<Unit> =
        withContext(Dispatchers.IO) {
            runCatching { followUpDao.insert(followUp) }
        }

    override fun getAllPatient(): Flow<List<Patient>> =
        patientDao.getAllPatient().catch { exception ->
            throw Exception("Failed to get all patients", exception)
        }
            .flowOn(Dispatchers.IO)


    override fun getPatientWithDetail(patientId: Long): Flow<PatientWithDetail> =
        patientDao.getPatientWithDetail(patientId).catch { exception ->
            throw Exception("Failed to get patient with detail", exception)
        }
            .flowOn(Dispatchers.IO)

    override fun getPatientWithVisitAndFollowUp(): Flow<List<PatientWithVisitAndFollowUp>> =
        patientDao.getPatientWithVisitAndFollowUp().catch { exception ->
            throw Exception("Failed to get patient with visit and follow up", exception)
        }
            .flowOn(Dispatchers.IO)

    override fun getAllFollowUp(): Flow<List<FollowUp>> =
        followUpDao.getAllFollowUp().catch { exception ->
            throw Exception("Failed to get all follow up", exception)
        }
            .flowOn(Dispatchers.IO)


    override fun getAllVisit(): Flow<List<Visit>> =
        visitDao.getAllVisit().catch { exception ->
            throw Exception("Failed to get all visit", exception)
        }
            .flowOn(Dispatchers.IO)

    override fun getPatientById(patientId: Long): Flow<Patient> =
        patientDao.getPatientById(patientId).catch { exception ->
            throw Exception("Failed to get patient by id", exception)
        }
            .flowOn(Dispatchers.IO)


}