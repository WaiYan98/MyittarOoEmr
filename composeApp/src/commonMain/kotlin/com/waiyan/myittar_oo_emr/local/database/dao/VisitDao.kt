package com.waiyan.myittar_oo_emr.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.waiyan.myittar_oo_emr.data.entity.Visit
import kotlinx.coroutines.flow.Flow

@Dao
interface VisitDao {

    @Upsert
    suspend fun insert(visit: Visit)

    @Query("SELECT * FROM visit")
    fun getAllVisit(): Flow<List<Visit>>
}