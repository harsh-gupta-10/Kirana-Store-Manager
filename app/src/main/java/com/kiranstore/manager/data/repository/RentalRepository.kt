package com.kiranstore.manager.data.repository

import com.kiranstore.manager.data.database.dao.RentalDao
import com.kiranstore.manager.data.database.dao.RentalMachineDao
import com.kiranstore.manager.data.database.entities.Rental
import com.kiranstore.manager.data.database.entities.RentalMachine
import com.kiranstore.manager.data.database.entities.RentalStatus
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RentalRepository @Inject constructor(
    private val rentalDao: RentalDao,
    private val rentalMachineDao: RentalMachineDao
) {
    // Rentals
    fun getAllRentals(): Flow<List<Rental>> = rentalDao.getAllRentals()

    fun getRentalsByStatus(status: RentalStatus): Flow<List<Rental>> =
        rentalDao.getRentalsByStatus(status)

    fun getRentalsForCustomer(customerId: Long): Flow<List<Rental>> =
        rentalDao.getRentalsForCustomer(customerId)

    fun getActiveRentalCount(): Flow<Int> = rentalDao.getActiveRentalCount()

    fun getTotalDepositsHeld(): Flow<Double> = rentalDao.getTotalDepositsHeld()

    suspend fun getRentalById(id: Long): Rental? = rentalDao.getRentalById(id)

    suspend fun insertRental(rental: Rental): Long = rentalDao.insertRental(rental)

    suspend fun updateRental(rental: Rental) = rentalDao.updateRental(rental)

    suspend fun deleteRental(rental: Rental) = rentalDao.deleteRental(rental)

    // Machines
    fun getAllMachines(): Flow<List<RentalMachine>> = rentalMachineDao.getAllMachines()

    suspend fun getMachineById(id: Long): RentalMachine? = rentalMachineDao.getMachineById(id)

    suspend fun insertMachine(machine: RentalMachine): Long =
        rentalMachineDao.insertMachine(machine)

    suspend fun updateMachine(machine: RentalMachine) =
        rentalMachineDao.updateMachine(machine)

    suspend fun deleteMachine(machine: RentalMachine) =
        rentalMachineDao.deleteMachine(machine)
}
