package com.waiyan.myittar_oo_emr.local.database.dao

import androidx.room.Dao
import androidx.room.Upsert
import com.waiyan.myittar_oo_emr.data.entity.Visit

@Dao
interface VisitDao {

    @Upsert
    suspend fun insert(visit: Visit)
}