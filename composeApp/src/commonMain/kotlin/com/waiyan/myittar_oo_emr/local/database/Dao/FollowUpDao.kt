package com.waiyan.myittar_oo_emr.local.database.Dao

import androidx.room.Dao
import androidx.room.Upsert
import com.waiyan.myittar_oo_emr.data.entity.FollowUp

@Dao
interface FollowUpDao {

    @Upsert
    suspend fun insert(followUp: FollowUp)

}