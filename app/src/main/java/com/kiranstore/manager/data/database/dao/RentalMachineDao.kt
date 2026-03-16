package com.kiranstore.manager.data.database.dao

import androidx.room.*
import com.kiranstore.manager.data.database.entities.RentalMachine
import kotlinx.coroutines.flow.Flow

@Dao
interface RentalMachineDao {

    @Query("SELECT * FROM rental_machines ORDER BY name ASC")
    fun getAllMachines(): Flow<List<RentalMachine>>

    @Query("SELECT * FROM rental_machines WHERE id = :id")
    suspend fun getMachineById(id: Long): RentalMachine?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMachine(machine: RentalMachine): Long

    @Update
    suspend fun updateMachine(machine: RentalMachine)

    @Delete
    suspend fun deleteMachine(machine: RentalMachine)
}
