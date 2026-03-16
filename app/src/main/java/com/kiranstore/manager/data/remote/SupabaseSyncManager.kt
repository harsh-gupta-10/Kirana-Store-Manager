package com.kiranstore.manager.data.remote

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.Serializable
import javax.inject.Inject

sealed class SyncState {
    object Idle : SyncState()
    object Syncing : SyncState()
    data class Success(val message: String) : SyncState()
    data class Error(val message: String) : SyncState()
}

@Serializable
data class RemoteCustomer(
    val id: Long? = null,
    val local_id: Long,
    val name: String,
    val phone: String,
    val notes: String = "",
    val created_at: Long = System.currentTimeMillis()
)

@Serializable
data class RemoteDebt(
    val id: Long? = null,
    val local_id: Long,
    val customer_local_id: Long,
    val item_name: String,
    val amount: Double,
    val date: Long = System.currentTimeMillis(),
    val notes: String = ""
)

@Serializable
data class RemotePayment(
    val id: Long? = null,
    val local_id: Long,
    val customer_local_id: Long,
    val amount: Double,
    val date: Long = System.currentTimeMillis(),
    val notes: String = ""
)

class SupabaseSyncManager @Inject constructor(
    private val client: SupabaseClient
) {
    private val _syncState = MutableStateFlow<SyncState>(SyncState.Idle)
    val syncState: StateFlow<SyncState> = _syncState.asStateFlow()

    suspend fun syncCustomers(customers: List<RemoteCustomer>): Result<Unit> {
        _syncState.value = SyncState.Syncing
        return try {
            client.postgrest["customers"].upsert(customers)
            _syncState.value = SyncState.Success("Customers synced")
            Result.success(Unit)
        } catch (e: Exception) {
            _syncState.value = SyncState.Error(e.message ?: "Sync failed")
            Result.failure(e)
        }
    }

    suspend fun syncDebts(debts: List<RemoteDebt>): Result<Unit> {
        _syncState.value = SyncState.Syncing
        return try {
            client.postgrest["debts"].upsert(debts)
            _syncState.value = SyncState.Success("Debts synced")
            Result.success(Unit)
        } catch (e: Exception) {
            _syncState.value = SyncState.Error(e.message ?: "Sync failed")
            Result.failure(e)
        }
    }

    suspend fun syncPayments(payments: List<RemotePayment>): Result<Unit> {
        _syncState.value = SyncState.Syncing
        return try {
            client.postgrest["payments"].upsert(payments)
            _syncState.value = SyncState.Success("Payments synced")
            Result.success(Unit)
        } catch (e: Exception) {
            _syncState.value = SyncState.Error(e.message ?: "Sync failed")
            Result.failure(e)
        }
    }

    suspend fun fetchRemoteCustomers(): Result<List<RemoteCustomer>> {
        return try {
            val result = client.postgrest["customers"]
                .select()
                .decodeList<RemoteCustomer>()
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun resetState() {
        _syncState.value = SyncState.Idle
    }
}
