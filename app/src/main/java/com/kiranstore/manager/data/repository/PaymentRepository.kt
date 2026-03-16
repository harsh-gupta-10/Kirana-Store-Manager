package com.kiranstore.manager.data.repository

import com.kiranstore.manager.data.database.dao.PaymentDao
import com.kiranstore.manager.data.database.entities.Payment
import kotlinx.coroutines.flow.Flow
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PaymentRepository @Inject constructor(
    private val paymentDao: PaymentDao
) {
    fun getPaymentsForCustomer(customerId: Long): Flow<List<Payment>> =
        paymentDao.getPaymentsForCustomer(customerId)

    fun getTotalPaymentsForCustomer(customerId: Long): Flow<Double> =
        paymentDao.getTotalPaymentsForCustomer(customerId)

    fun getPaymentsRecoveredToday(): Flow<Double> {
        val cal = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return paymentDao.getPaymentsRecoveredToday(cal.timeInMillis)
    }

    suspend fun insertPayment(payment: Payment): Long =
        paymentDao.insertPayment(payment)

    suspend fun deletePayment(payment: Payment) =
        paymentDao.deletePayment(payment)
}
