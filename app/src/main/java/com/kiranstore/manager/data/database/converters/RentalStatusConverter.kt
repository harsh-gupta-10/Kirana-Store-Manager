package com.kiranstore.manager.data.database.converters

import androidx.room.TypeConverter
import com.kiranstore.manager.data.database.entities.RentalStatus

/**
 * TypeConverter for RentalStatus enum to store it as a string in the Room database.
 * This allows Room to properly serialize and deserialize the RentalStatus enum.
 */
class RentalStatusConverter {

    @TypeConverter
    fun fromRentalStatus(status: RentalStatus): String {
        return status.name
    }

    @TypeConverter
    fun toRentalStatus(status: String): RentalStatus {
        return RentalStatus.valueOf(status)
    }
}
