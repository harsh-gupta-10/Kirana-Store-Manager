package com.kiranstore.manager.data.database.dao

import androidx.room.*
import com.kiranstore.manager.data.database.entities.Payment
import kotlinx.coroutines.flow.Flow

@Dao
interface PaymentDao {

    @Query("SELECT * FROM payments WHERE customerId = :customerId ORDER BY date DESC")
    fun getPaymentsForCustomer(customerId: Long): Flow<List<Payment>>

    @Query("SELECT COALESCE(SUM(amount), 0) FROM payments WHERE customerId = :customerId")
    fun getTotalPaymentsForCustomer(customerId: Long): Flow<Double>

    @Query("SELECT COALESCE(SUM(amount), 0) FROM payments WHERE date >= :startOfDay")
    fun getPaymentsRecoveredToday(startOfDay: Long): Flow<Double>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayment(payment: Payment): Long

    @Delete
    suspend fun deletePayment(payment: Payment)
}
