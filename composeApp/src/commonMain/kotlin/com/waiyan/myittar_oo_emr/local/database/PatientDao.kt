package com.waiyan.myittar_oo_emr.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.waiyan.myittar_oo_emr.data.Patient
import kotlinx.coroutines.flow.Flow

@Dao
interface PatientDao {

    @Insert
    suspend fun insertPatient(patient: Patient)

    @Query("SELECT * FROM Patient")
    fun getAllPatient(): Flow<List<Patient>>

}