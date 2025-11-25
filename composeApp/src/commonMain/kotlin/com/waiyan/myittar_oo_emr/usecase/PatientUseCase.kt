package com.waiyan.myittar_oo_emr.usecase

import com.waiyan.myittar_oo_emr.data.entity.Patient
import com.waiyan.myittar_oo_emr.local_service.EmrRepository
import kotlinx.coroutines.flow.first

class PatientUseCase(
    private val emrRepository: EmrRepository
) {

    suspend fun getAllPatients(): Result<List<Patient>> = runCatching {
        emrRepository.getAllPatient().first()
    }
}