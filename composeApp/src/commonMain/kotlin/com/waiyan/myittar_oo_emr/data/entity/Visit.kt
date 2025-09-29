package com.waiyan.myittar_oo_emr.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Visit(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val patientId: Long,
    val date: String,
    val diagnosis: String,
    val prescription: String,
    val fee: Long
)
