package com.kiranstore.manager.data.remote.supabase

import com.kiranstore.manager.data.database.entities.*
import com.kiranstore.manager.data.repository.CustomerRepository
import com.kiranstore.manager.data.repository.DebtRepository
import com.kiranstore.manager.data.repository.PaymentRepository
import com.kiranstore.manager.data.repository.RentalRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SupabaseSyncRepository @Inject constructor(
    private val client: SupabaseClient?,
    private val customerRepository: CustomerRepository,
    private val debtRepository: DebtRepository,
    private val paymentRepository: PaymentRepository,
    private val rentalRepository: RentalRepository
) {

    suspend fun syncAll(): Result<Unit> = withContext(Dispatchers.IO) {
        val supabase = client ?: return@withContext Result.failure(
            IllegalStateException("Supabase credentials are missing; skipping cloud sync.")
        )

        val customers = customerRepository.getAllCustomers().first()
        val debts = debtRepository.getAllDebts().first()
        val payments = paymentRepository.getAllPayments().first()
        val rentals = rentalRepository.getAllRentals().first()
        val machines = rentalRepository.getAllMachines().first()

        runCatching {
            supabase.postgrest["customers"].upsert(customers.map { it.toRemote() })
            supabase.postgrest["debts"].upsert(debts.map { it.toRemote() })
            supabase.postgrest["payments"].upsert(payments.map { it.toRemote() })
            supabase.postgrest["rentals"].upsert(rentals.map { it.toRemote() })
            supabase.postgrest["rental_machines"].upsert(machines.map { it.toRemote() })
        }
    }

    private fun Customer.toRemote(): Map<String, Any?> = mapOf(
        "id" to id,
        "name" to name,
        "phone" to phone,
        "notes" to notes
    )

    private fun Debt.toRemote(): Map<String, Any?> = mapOf(
        "id" to id,
        "customerId" to customerId,
        "amount" to amount,
        "date" to date,
        "notes" to notes
    )

    private fun Payment.toRemote(): Map<String, Any?> = mapOf(
        "id" to id,
        "customerId" to customerId,
        "amount" to amount,
        "date" to date,
        "notes" to notes
    )

    private fun Rental.toRemote(): Map<String, Any?> = mapOf(
        "id" to id,
        "customerId" to customerId,
        "machineId" to machineId,
        "startDate" to startDate,
        "endDate" to endDate,
        "depositAmount" to depositAmount,
        "rentAmount" to rentAmount,
        "status" to status.name
    )

    private fun RentalMachine.toRemote(): Map<String, Any?> = mapOf(
        "id" to id,
        "name" to name,
        "description" to description,
        "dailyRate" to dailyRate
    )
}
