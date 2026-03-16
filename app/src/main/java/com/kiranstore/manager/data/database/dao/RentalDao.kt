package com.kiranstore.manager.data.database.dao

import androidx.room.*
import com.kiranstore.manager.data.database.entities.Rental
import com.kiranstore.manager.data.database.entities.RentalStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface RentalDao {

    @Query("SELECT * FROM rentals ORDER BY startDate DESC")
    fun getAllRentals(): Flow<List<Rental>>

    @Query("SELECT * FROM rentals WHERE status = :status ORDER BY startDate DESC")
    fun getRentalsByStatus(status: RentalStatus): Flow<List<Rental>>

    @Query("SELECT * FROM rentals WHERE customerId = :customerId ORDER BY startDate DESC")
    fun getRentalsForCustomer(customerId: Long): Flow<List<Rental>>

    @Query("SELECT COUNT(*) FROM rentals WHERE status = 'ACTIVE'")
    fun getActiveRentalCount(): Flow<Int>

    @Query("SELECT COALESCE(SUM(depositAmount), 0) FROM rentals WHERE status = 'ACTIVE'")
    fun getTotalDepositsHeld(): Flow<Double>

    @Query("SELECT * FROM rentals WHERE id = :id")
    suspend fun getRentalById(id: Long): Rental?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRental(rental: Rental): Long

    @Update
    suspend fun updateRental(rental: Rental)

    @Delete
    suspend fun deleteRental(rental: Rental)
}
