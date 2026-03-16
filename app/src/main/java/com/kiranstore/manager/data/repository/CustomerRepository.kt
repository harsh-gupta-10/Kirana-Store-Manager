package com.kiranstore.manager.data.repository

import com.kiranstore.manager.data.database.dao.CustomerDao
import com.kiranstore.manager.data.database.entities.Customer
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CustomerRepository @Inject constructor(
    private val customerDao: CustomerDao
) {
    fun getAllCustomers(): Flow<List<Customer>> = customerDao.getAllCustomers()

    fun searchCustomers(query: String): Flow<List<Customer>> =
        customerDao.searchCustomers(query)

    suspend fun getCustomerById(id: Long): Customer? =
        customerDao.getCustomerById(id)

    suspend fun insertCustomer(customer: Customer): Long =
        customerDao.insertCustomer(customer)

    suspend fun updateCustomer(customer: Customer) =
        customerDao.updateCustomer(customer)

    suspend fun deleteCustomer(customer: Customer) =
        customerDao.deleteCustomer(customer)
}
