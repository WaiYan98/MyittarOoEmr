package com.waiyan.myittar_oo_emr.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Patient(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    val age: Int,
    val phone: String,
    val address: String
)