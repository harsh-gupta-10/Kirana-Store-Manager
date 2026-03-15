package com.kiranstore.manager.data.repository

import com.kiranstore.manager.data.database.dao.*
import com.kiranstore.manager.data.database.entities.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

// ─────────────────────────────────────────────────────────
// SHOP REPOSITORY
// ─────────────────────────────────────────────────────────
@Singleton
class ShopRepository @Inject constructor(private val shopDao: ShopDao) {

    fun getShopByUserId(userId: String): Flow<ShopEntity?> =
        shopDao.getShopByUserId(userId)

    suspend fun saveShop(shop: ShopEntity): Long =
        shopDao.insertShop(shop)

    suspend fun updateShop(shop: ShopEntity) =
        shopDao.updateShop(shop)
}

// ─────────────────────────────────────────────────────────
// CUSTOMER REPOSITORY
// ─────────────────────────────────────────────────────────
@Singleton
class CustomerRepository @Inject constructor(private val customerDao: CustomerDao) {

    fun getAllCustomers(shopId: Long): Flow<List<CustomerEntity>> =
        customerDao.getAllCustomers(shopId)

    fun searchCustomers(shopId: Long, query: String): Flow<List<CustomerEntity>> =
        customerDao.searchCustomers(shopId, query)

    suspend fun addCustomer(customer: CustomerEntity): Long =
        customerDao.insertCustomer(customer)

    suspend fun updateCustomer(customer: CustomerEntity) =
        customerDao.updateCustomer(customer)

    suspend fun deleteCustomer(customer: CustomerEntity) =
        customerDao.deleteCustomer(customer)

    suspend fun getById(id: Long): CustomerEntity? =
        customerDao.getCustomerById(id)

    suspend fun findByName(shopId: Long, name: String): CustomerEntity? =
        customerDao.findCustomerByName(shopId, name)

    fun getCustomerCount(shopId: Long): Flow<Int> =
        customerDao.getCustomerCount(shopId)
}

// ─────────────────────────────────────────────────────────
// DEBT REPOSITORY
// ─────────────────────────────────────────────────────────
@Singleton
class DebtRepository @Inject constructor(private val debtDao: DebtDao) {

    fun getAllDebts(shopId: Long): Flow<List<DebtEntity>> =
        debtDao.getAllDebts(shopId)

    fun getDebtsByCustomer(customerId: Long): Flow<List<DebtEntity>> =
        debtDao.getDebtsByCustomer(customerId)

    fun getTotalOutstanding(shopId: Long): Flow<Double?> =
        debtDao.getTotalOutstanding(shopId)

    fun getCustomerBalance(customerId: Long): Flow<Double?> =
        debtDao.getCustomerBalance(customerId)

    fun getRecoveredToday(shopId: Long, todayStart: Long): Flow<Double?> =
        debtDao.getRecoveredToday(shopId, todayStart)

    fun getPaymentsByCustomer(customerId: Long): Flow<List<DebtPaymentEntity>> =
        debtDao.getPaymentsByCustomer(customerId)

    fun searchDebts(shopId: Long, query: String): Flow<List<DebtEntity>> =
        debtDao.searchDebtsByCustomerName(shopId, query)

    fun getRecentDebts(shopId: Long, limit: Int = 5): Flow<List<DebtEntity>> =
        debtDao.getRecentDebts(shopId, limit)

    fun getRecentPayments(shopId: Long, limit: Int = 5): Flow<List<DebtPaymentEntity>> =
        debtDao.getRecentPayments(shopId, limit)

    suspend fun addDebt(debt: DebtEntity): Long =
        debtDao.insertDebt(debt)

    suspend fun markAsPaid(debtId: Long) =
        debtDao.markAsPaid(debtId)

    suspend fun addPayment(payment: DebtPaymentEntity): Long =
        debtDao.insertPayment(payment)

    suspend fun deleteDebt(debt: DebtEntity) =
        debtDao.deleteDebt(debt)
}

// ─────────────────────────────────────────────────────────
// RENTAL REPOSITORY
// ─────────────────────────────────────────────────────────
@Singleton
class RentalRepository @Inject constructor(private val rentalDao: RentalDao) {

    fun getAllRentals(shopId: Long): Flow<List<RentalEntity>> =
        rentalDao.getAllRentals(shopId)

    fun getRentalsByStatus(shopId: Long, status: String): Flow<List<RentalEntity>> =
        rentalDao.getRentalsByStatus(shopId, status)

    fun getActiveRentalCount(shopId: Long): Flow<Int> =
        rentalDao.getActiveRentalCount(shopId)

    fun getTotalDepositsHeld(shopId: Long): Flow<Double?> =
        rentalDao.getTotalDepositsHeld(shopId)

    fun getAllMachines(shopId: Long): Flow<List<RentalMachineEntity>> =
        rentalDao.getAllMachines(shopId)

    suspend fun addMachine(machine: RentalMachineEntity): Long =
        rentalDao.insertMachine(machine)

    suspend fun addRental(rental: RentalEntity): Long =
        rentalDao.insertRental(rental)

    suspend fun updateRental(rental: RentalEntity) =
        rentalDao.updateRental(rental)

    suspend fun updateStatus(rentalId: Long, status: String, returnDate: Long? = null) =
        rentalDao.updateRentalStatus(rentalId, status, returnDate)

    suspend fun deleteRental(rental: RentalEntity) =
        rentalDao.deleteRental(rental)
}

// ─────────────────────────────────────────────────────────
// BUY LIST REPOSITORY
// ─────────────────────────────────────────────────────────
@Singleton
class BuyListRepository @Inject constructor(private val buyListDao: BuyListDao) {

    fun getAllItems(shopId: Long): Flow<List<BuyListItemEntity>> =
        buyListDao.getAllItems(shopId)

    fun getPendingItems(shopId: Long): Flow<List<BuyListItemEntity>> =
        buyListDao.getPendingItems(shopId)

    fun getPendingCount(shopId: Long): Flow<Int> =
        buyListDao.getPendingCount(shopId)

    suspend fun addItem(item: BuyListItemEntity): Long =
        buyListDao.insertItem(item)

    suspend fun updateItem(item: BuyListItemEntity) =
        buyListDao.updateItem(item)

    suspend fun markAsPurchased(itemId: Long) =
        buyListDao.markAsPurchased(itemId)

    suspend fun clearPurchased(shopId: Long) =
        buyListDao.clearPurchased(shopId)

    suspend fun deleteItem(item: BuyListItemEntity) =
        buyListDao.deleteItem(item)
}

// ─────────────────────────────────────────────────────────
// TASK REPOSITORY
// ─────────────────────────────────────────────────────────
@Singleton
class TaskRepository @Inject constructor(private val taskDao: TaskDao) {

    fun getAllTasks(shopId: Long): Flow<List<TaskEntity>> =
        taskDao.getAllTasks(shopId)

    fun getTasksForDay(shopId: Long, dayStart: Long, dayEnd: Long): Flow<List<TaskEntity>> =
        taskDao.getTasksForDay(shopId, dayStart, dayEnd)

    fun getTodayPendingCount(shopId: Long, dayStart: Long, dayEnd: Long): Flow<Int> =
        taskDao.getTodayPendingCount(shopId, dayStart, dayEnd)

    suspend fun addTask(task: TaskEntity): Long =
        taskDao.insertTask(task)

    suspend fun updateTask(task: TaskEntity) =
        taskDao.updateTask(task)

    suspend fun markAsDone(taskId: Long) =
        taskDao.markAsDone(taskId)

    suspend fun deleteTask(task: TaskEntity) =
        taskDao.deleteTask(task)
}
