package com.waiyan.myittar_oo_emr.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.waiyan.myittar_oo_emr.data.MonthlyIncome
import com.waiyan.myittar_oo_emr.data.entity.Visit
import kotlinx.coroutines.flow.Flow

@Dao
interface VisitDao {

    @Upsert
    suspend fun upsert(visit: Visit)

    @Query("SELECT * FROM visit")
    fun getAllVisit(): Flow<List<Visit>>

    @Query("SELECT * FROM visit")
    suspend fun getAll(): List<Visit>

    @Query(
        "SELECT STRFTIME('%Y-%m', date/1000, 'unixepoch') as month, SUM(fee) as income " +
                "FROM visit " +
                "WHERE date BETWEEN :startDate AND :endDate " +
                "GROUP BY month ORDER BY month DESC"
    )
    fun getMonthlyIncome(startDate: Long, endDate: Long): Flow<List<MonthlyIncome>>

    @Query("DELETE FROM visit WHERE patientId IN (:patientIds)")
    suspend fun deleteVisitsByIds(patientIds: List<Long>)
}