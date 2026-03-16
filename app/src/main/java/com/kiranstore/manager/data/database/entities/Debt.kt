package com.kiranstore.manager.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "debts",
    foreignKeys = [
        ForeignKey(
            entity = Customer::class,
            parentColumns = ["id"],
            childColumns = ["customerId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("customerId")]
)
data class Debt(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val customerId: Long,
    val itemName: String,
    val amount: Double,
    val date: Long = System.currentTimeMillis(),
    val notes: String = ""
)
