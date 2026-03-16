package com.kiranstore.manager.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

enum class RentalStatus { ACTIVE, RETURNED, LATE }

@Entity(
    tableName = "rentals",
    foreignKeys = [
        ForeignKey(
            entity = Customer::class,
            parentColumns = ["id"],
            childColumns = ["customerId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = RentalMachine::class,
            parentColumns = ["id"],
            childColumns = ["machineId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("customerId"), Index("machineId")]
)
data class Rental(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val customerId: Long,
    val machineId: Long,
    val rentAmount: Double,
    val depositAmount: Double,
    val startDate: Long = System.currentTimeMillis(),
    val expectedReturnDate: Long,
    val actualReturnDate: Long? = null,
    val additionalCharges: Double = 0.0,
    val damageNotes: String = "",
    val notes: String = "",
    val status: RentalStatus = RentalStatus.ACTIVE
)
