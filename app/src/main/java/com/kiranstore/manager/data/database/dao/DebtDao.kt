package com.kiranstore.manager.data.database.dao

import androidx.room.*
import com.kiranstore.manager.data.database.entities.Debt
import kotlinx.coroutines.flow.Flow

@Dao
interface DebtDao {

    @Query("SELECT * FROM debts WHERE customerId = :customerId ORDER BY date DESC")
    fun getDebtsForCustomer(customerId: Long): Flow<List<Debt>>

    @Query("SELECT * FROM debts ORDER BY date DESC LIMIT 5")
    fun getRecentDebts(): Flow<List<Debt>>

    @Query("SELECT COALESCE(SUM(amount), 0) FROM debts WHERE customerId = :customerId")
    fun getTotalDebtForCustomer(customerId: Long): Flow<Double>

    @Query("SELECT COALESCE(SUM(amount), 0) FROM debts")
    fun getTotalAllDebts(): Flow<Double>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDebt(debt: Debt): Long

    @Update
    suspend fun updateDebt(debt: Debt)

    @Delete
    suspend fun deleteDebt(debt: Debt)
}
