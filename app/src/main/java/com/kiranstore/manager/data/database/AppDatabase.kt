package com.kiranstore.manager.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kiranstore.manager.data.database.converters.RentalStatusConverter
import com.kiranstore.manager.data.database.dao.*
import com.kiranstore.manager.data.database.entities.*

@Database(
    entities = [
        Customer::class,
        Debt::class,
        Payment::class,
        RentalMachine::class,
        Rental::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(RentalStatusConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun customerDao(): CustomerDao
    abstract fun debtDao(): DebtDao
    abstract fun paymentDao(): PaymentDao
    abstract fun rentalMachineDao(): RentalMachineDao
    abstract fun rentalDao(): RentalDao

    companion object {
        const val DATABASE_NAME = "kiran_store_db"
    }
}
