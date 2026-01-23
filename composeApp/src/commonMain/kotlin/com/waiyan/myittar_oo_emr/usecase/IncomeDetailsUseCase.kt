package com.waiyan.myittar_oo_emr.usecase

import com.waiyan.myittar_oo_emr.data.IncomeDetail
import com.waiyan.myittar_oo_emr.local_service.EmrRepository
import kotlinx.coroutines.flow.first

class IncomeDetailsUseCase(
    private val emrRepository: EmrRepository
) {
    suspend operator fun invoke(): Result<List<IncomeDetail>> = runCatching {
        val patientsWithVisits = emrRepository.getPatientWithVisitAndFollowUp().first()

        patientsWithVisits.flatMap { patientWithVisit ->
            patientWithVisit.visits.map { visit ->
                IncomeDetail(
                    patientName = patientWithVisit.patient.name,
                    visitTime = visit.date,
                    fee = visit.fee
                )
            }
        }
    }
}
