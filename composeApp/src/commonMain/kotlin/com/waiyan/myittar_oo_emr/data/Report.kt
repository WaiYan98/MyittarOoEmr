package com.waiyan.myittar_oo_emr.data

data class Report(
    val todayPatientsSeen: Int,
    val todayIncome: Long,
    val thisMonthIncome: Long,
    val thisYearIncome: Long,
    val upcomingFollowUps: List<UpcomingFollowUp>
)
