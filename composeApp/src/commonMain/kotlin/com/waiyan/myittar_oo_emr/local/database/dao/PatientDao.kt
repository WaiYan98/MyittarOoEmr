package com.waiyan.myittar_oo_emr.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.waiyan.myittar_oo_emr.data.PatientWithDetail
import com.waiyan.myittar_oo_emr.data.PatientWithVisitAndFollowUp
import com.waiyan.myittar_oo_emr.data.entity.Patient
import kotlinx.coroutines.flow.Flow

@Dao
interface PatientDao {

    @Insert
    suspend fun insertPatient(patient: Patient): Long

    @Query("SELECT * FROM Patient")
    fun getAllPatient(): Flow<List<Patient>>

    @Transaction
    @Query("SELECT * FROM Patient WHERE id=:patientId")
    fun getPatientWithDetail(patientId: Long): Flow<PatientWithDetail>

    @Transaction
    @Query("SELECT * FROM Patient")
    fun getPatientWithVisitAndFollowUp(): Flow<List<PatientWithVisitAndFollowUp>>
}