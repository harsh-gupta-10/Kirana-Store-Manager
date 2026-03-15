package com.kiranstore.manager.data.database.dao

import androidx.room.*
import com.kiranstore.manager.data.database.entities.DebtEntity
import com.kiranstore.manager.data.database.entities.DebtPaymentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DebtDao {

    // ── Debts ──────────────────────────────────────────────
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDebt(debt: DebtEntity): Long

    @Update
    suspend fun updateDebt(debt: DebtEntity)

    @Delete
    suspend fun deleteDebt(debt: DebtEntity)

    @Query("SELECT * FROM debts WHERE shopId = :shopId ORDER BY date DESC")
    fun getAllDebts(shopId: Long): Flow<List<DebtEntity>>

    @Query("SELECT * FROM debts WHERE customerId = :customerId ORDER BY date DESC")
    fun getDebtsByCustomer(customerId: Long): Flow<List<DebtEntity>>

    @Query("SELECT (SELECT IFNULL(SUM(amount), 0) FROM debts WHERE shopId = :shopId AND status = 'PENDING') - (SELECT IFNULL(SUM(amount), 0) FROM debt_payments WHERE shopId = :shopId)")
    fun getTotalOutstanding(shopId: Long): Flow<Double?>

    @Query("SELECT (SELECT IFNULL(SUM(amount), 0) FROM debts WHERE customerId = :customerId AND status = 'PENDING') - (SELECT IFNULL(SUM(amount), 0) FROM debt_payments WHERE customerId = :customerId)")
    fun getCustomerBalance(customerId: Long): Flow<Double?>

    @Query("UPDATE debts SET status = 'PAID' WHERE id = :debtId")
    suspend fun markAsPaid(debtId: Long)

    @Query("""
        SELECT d.* FROM debts d
        INNER JOIN customers c ON d.customerId = c.id
        WHERE d.shopId = :shopId AND c.name LIKE '%' || :query || '%'
        ORDER BY d.date DESC
    """)
    fun searchDebtsByCustomerName(shopId: Long, query: String): Flow<List<DebtEntity>>

    @Query("SELECT * FROM debts WHERE shopId = :shopId ORDER BY date DESC LIMIT :limit")
    fun getRecentDebts(shopId: Long, limit: Int = 5): Flow<List<DebtEntity>>

    // ── Payments ───────────────────────────────────────────
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayment(payment: DebtPaymentEntity): Long

    @Query("SELECT * FROM debt_payments WHERE customerId = :customerId ORDER BY date DESC")
    fun getPaymentsByCustomer(customerId: Long): Flow<List<DebtPaymentEntity>>

    @Query("SELECT * FROM debt_payments WHERE shopId = :shopId ORDER BY date DESC LIMIT :limit")
    fun getRecentPayments(shopId: Long, limit: Int = 5): Flow<List<DebtPaymentEntity>>

    @Query("SELECT SUM(amount) FROM debt_payments WHERE customerId = :customerId")
    fun getTotalPayments(customerId: Long): Flow<Double?>

    @Query("SELECT SUM(amount) FROM debt_payments WHERE shopId = :shopId AND date >= :todayStart")
    fun getRecoveredToday(shopId: Long, todayStart: Long): Flow<Double?>
}
