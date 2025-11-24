package com.waiyan.myittar_oo_emr.usecase

import com.waiyan.myittar_oo_emr.data.Report
import com.waiyan.myittar_oo_emr.data.UpcomingFollowUp
import com.waiyan.myittar_oo_emr.data.entity.FollowUp
import com.waiyan.myittar_oo_emr.data.entity.Visit
import com.waiyan.myittar_oo_emr.local_service.EmrRepository
import com.waiyan.myittar_oo_emr.util.LocalTime
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull

class ReportUseCase(
    private val emrRepository: EmrRepository
) {

    suspend fun getReport(): Result<Report> = runCatching {
        coroutineScope {
            val visitDeferred = async { emrRepository.getAllVisit().first() }
            val followUpDeferred = async { emrRepository.getAllFollowUp().first() }

            val visits = visitDeferred.await()
            val followUps = followUpDeferred.await()

            if (visits.isEmpty() && followUps.isEmpty()) {
                return@coroutineScope Report(
                    0,
                    0,
                    0,
                    0,
                    emptyList()
                )
            }

            val todayPatientSeenDeferred = async { calculateTodayPatientSeen(visits) }
            val todayIncomeDeferred = async { calculateTodayIncome(visits) }
            val thisMonthIncomeDeferred = async { calculateThisMonthIncome(visits) }
            val thisYearIncomeDeferred = async { calculateThisYearIncome(visits) }
            val upcomingFollowUpsDeferred = async { changeToUpComingFollowUps(followUps) }

            Report(
                todayPatientsSeen = todayPatientSeenDeferred.await(),
                todayIncome = todayIncomeDeferred.await(),
                thisMonthIncome = thisMonthIncomeDeferred.await(),
                thisYearIncome = thisYearIncomeDeferred.await(),
                upcomingFollowUps = upcomingFollowUpsDeferred.await()
            )
        }
    }

    private suspend fun getPatientNameById(patientId: Long): String {
        val patient = emrRepository.getPatientById(patientId).firstOrNull()
            ?: return "Unknown"
        return patient.name
    }


    private suspend fun changeToUpComingFollowUps(followUps: List<FollowUp>): List<UpcomingFollowUp> {
        return followUps
            .filter { followUp -> followUp.date >= LocalTime.getCurrentTimeMillis() }
            .map { followUp ->
                UpcomingFollowUp(
                    followUpDate = LocalTime.getHumanDate(followUp.date),
                    patientName = getPatientNameById(followUp.patientId),
                    reasonForFollowUp = followUp.reasonForFollowUp,
                    timeUntil = LocalTime.calculateDayUntil(followUp.date).toString()
                )
            }.sortedBy { it.timeUntil.toLongOrNull() ?: Long.MAX_VALUE }
    }

    private fun calculateTodayPatientSeen(visits: List<Visit>): Int {
        val startOfTodayTimeStamp = LocalTime.getStartOfTodayTimeStamp()
        val endOfTodayTimeStamp = LocalTime.getEndOfTodayTimeStamp()

        val todayVisits = visits.filter { visit ->
            visit.date in startOfTodayTimeStamp..<endOfTodayTimeStamp
        }
        return todayVisits.size
    }

    private fun calculateTodayIncome(visits: List<Visit>): Long {
        val startOfTodayTimeStamp = LocalTime.getStartOfTodayTimeStamp()
        val endOfTodayTimeStamp = LocalTime.getEndOfTodayTimeStamp()
        val todayIncome = visits.filter { visit ->
            visit.date in startOfTodayTimeStamp..<endOfTodayTimeStamp
        }.sumOf { visit ->
            visit.fee
        }
        return todayIncome

    }

    private fun calculateThisMonthIncome(visits: List<Visit>): Long {
        val currentMonth = LocalTime.getCurrentMonth()
        val currentYear = LocalTime.getCurrentYear()
        val thisMonthIncome = visits.filter { visit ->
            LocalTime.timestampToMonth(visit.date) == currentMonth &&
                    LocalTime.timestampToYear(visit.date) == currentYear
        }.sumOf { visit ->
            visit.fee
        }
        return thisMonthIncome
    }

    private fun calculateThisYearIncome(visits: List<Visit>): Long {
        val currentYear = LocalTime.getCurrentYear()
        val thisYearIncome = visits.filter { visit ->
            LocalTime.timestampToYear(visit.date) == currentYear
        }.sumOf { visit ->
            visit.fee
        }
        return thisYearIncome
    }

}