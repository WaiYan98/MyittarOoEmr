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
import kotlinx.coroutines.withContext

class EmrRepositoryImpl(
    private val patientDao: PatientDao,
    private val visitDao: VisitDao,
    private val medicalInfoDao: MedicalInfoDao,
    private val followUpDao: FollowUpDao
) : EmrRepository {

    override suspend fun insertPatient(patient: Patient): Result<Long> = withContext(Dispatchers.IO) {
            runCatching {
               patientDao.insertPatient(patient)
            }
        }

    override suspend fun insertMedicalInfo(medicalInfo: MedicalInfo) = withContext(Dispatchers.IO) {
        medicalInfoDao.insert(medicalInfo)
    }

    override suspend fun insertVisit(visit: Visit) = withContext(Dispatchers.IO) {
        visitDao.insert(visit)
    }

    override suspend fun insertFollowUp(followUp: FollowUp) = withContext(Dispatchers.IO) {
        followUpDao.insert(followUp)
    }

    override fun getAllPatient(): Flow<List<Patient>> = try {
        patientDao.getAllPatient()
    } catch (e: Exception) {
        throw Exception(e.message)
    }


    override fun getPatientWithDetail(patientId: Long): Flow<PatientWithDetail> =
        patientDao.getPatientWithDetail(patientId)

    override fun getPatientWithVisitAndFollowUp(): Flow<List<PatientWithVisitAndFollowUp>> =
        patientDao.getPatientWithVisitAndFollowUp()

}