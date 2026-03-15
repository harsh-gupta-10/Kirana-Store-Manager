package com.kiranstore.manager.data.database.dao

import androidx.room.*
import com.kiranstore.manager.data.database.entities.CustomerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomer(customer: CustomerEntity): Long

    @Update
    suspend fun updateCustomer(customer: CustomerEntity)

    @Delete
    suspend fun deleteCustomer(customer: CustomerEntity)

    @Query("SELECT * FROM customers WHERE shopId = :shopId ORDER BY name ASC")
    fun getAllCustomers(shopId: Long): Flow<List<CustomerEntity>>

    @Query("SELECT * FROM customers WHERE shopId = :shopId AND (name LIKE '%' || :query || '%' OR phone LIKE '%' || :query || '%')")
    fun searchCustomers(shopId: Long, query: String): Flow<List<CustomerEntity>>

    @Query("SELECT * FROM customers WHERE id = :customerId LIMIT 1")
    suspend fun getCustomerById(customerId: Long): CustomerEntity?

    @Query("SELECT * FROM customers WHERE shopId = :shopId AND name LIKE '%' || :name || '%' LIMIT 1")
    suspend fun findCustomerByName(shopId: Long, name: String): CustomerEntity?

    @Query("SELECT COUNT(*) FROM customers WHERE shopId = :shopId")
    fun getCustomerCount(shopId: Long): Flow<Int>
}
