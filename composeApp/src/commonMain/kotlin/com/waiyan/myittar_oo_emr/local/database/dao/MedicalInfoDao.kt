package com.waiyan.myittar_oo_emr.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Upsert
import com.waiyan.myittar_oo_emr.data.entity.MedicalInfo

@Dao
interface MedicalInfoDao {

    @Upsert
    suspend fun upsert(medicalInfo: MedicalInfo)

}