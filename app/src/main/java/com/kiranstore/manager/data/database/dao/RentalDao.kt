package com.kiranstore.manager.data.database.dao

import androidx.room.*
import com.kiranstore.manager.data.database.entities.RentalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RentalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRental(rental: RentalEntity): Long

    @Update
    suspend fun updateRental(rental: RentalEntity)

    @Delete
    suspend fun deleteRental(rental: RentalEntity)

    @Query("SELECT * FROM rentals WHERE shopId = :shopId ORDER BY startDate DESC")
    fun getAllRentals(shopId: Long): Flow<List<RentalEntity>>

    @Query("SELECT * FROM rentals WHERE shopId = :shopId AND status = :status ORDER BY startDate DESC")
    fun getRentalsByStatus(shopId: Long, status: String): Flow<List<RentalEntity>>

    @Query("SELECT COUNT(*) FROM rentals WHERE shopId = :shopId AND status = 'ACTIVE'")
    fun getActiveRentalCount(shopId: Long): Flow<Int>

    @Query("SELECT SUM(deposit) FROM rentals WHERE shopId = :shopId AND status = 'ACTIVE'")
    fun getTotalDepositsHeld(shopId: Long): Flow<Double?>

    @Query("SELECT * FROM rentals WHERE customerId = :customerId ORDER BY startDate DESC")
    fun getRentalsByCustomer(customerId: Long): Flow<List<RentalEntity>>

    @Query("UPDATE rentals SET status = :status, returnDate = :returnDate WHERE id = :rentalId")
    suspend fun updateRentalStatus(rentalId: Long, status: String, returnDate: Long?)

    @Query("""
        SELECT * FROM rentals 
        WHERE shopId = :shopId AND status = 'ACTIVE' 
        AND returnDate IS NOT NULL AND returnDate < :now
    """)
    suspend fun getOverdueRentals(shopId: Long, now: Long = System.currentTimeMillis()): List<RentalEntity>
}
