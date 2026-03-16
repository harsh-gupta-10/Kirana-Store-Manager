package com.kiranstore.manager.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rental_machines")
data class RentalMachine(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val rentPrice: Double,
    val deposit: Double,
    val createdAt: Long = System.currentTimeMillis()
)
