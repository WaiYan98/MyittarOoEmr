package com.waiyan.myittar_oo_emr.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.waiyan.myittar_oo_emr.data.entity.FollowUp
import kotlinx.coroutines.flow.Flow

@Dao
interface FollowUpDao {
    @Upsert
    suspend fun upsert(followUp: FollowUp)

    @Query("SELECT * FROM FollowUp ")
    fun getAllFollowUp(): Flow<List<FollowUp>>
}