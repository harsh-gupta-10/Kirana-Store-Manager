package com.kiranstore.manager.data.repository

import com.kiranstore.manager.data.database.dao.DebtDao
import com.kiranstore.manager.data.database.entities.Debt
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DebtRepository @Inject constructor(
    private val debtDao: DebtDao
) {
    fun getDebtsForCustomer(customerId: Long): Flow<List<Debt>> =
        debtDao.getDebtsForCustomer(customerId)

    fun getRecentDebts(): Flow<List<Debt>> =
        debtDao.getRecentDebts()

    fun getTotalDebtForCustomer(customerId: Long): Flow<Double> =
        debtDao.getTotalDebtForCustomer(customerId)

    fun getTotalAllDebts(): Flow<Double> =
        debtDao.getTotalAllDebts()

    suspend fun insertDebt(debt: Debt): Long =
        debtDao.insertDebt(debt)

    suspend fun updateDebt(debt: Debt) =
        debtDao.updateDebt(debt)

    suspend fun deleteDebt(debt: Debt) =
        debtDao.deleteDebt(debt)
}
