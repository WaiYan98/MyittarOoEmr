package com.waiyan.myittar_oo_emr.local_service

import com.waiyan.myittar_oo_emr.data.Patient
import com.waiyan.myittar_oo_emr.local.database.PatientDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class PatientRepositoryImpl(
    private val patientDao: PatientDao
) : PatientRepository {

    override suspend fun insertPatient(patient: Patient) = withContext(Dispatchers.IO) {
        patientDao.insertPatient(patient)
    }

    override fun getAllPatient(): Flow<List<Patient>> = patientDao.getAllPatient()

}