package com.kiranstore.manager.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.kiranstore.manager.data.database.dao.*
import com.kiranstore.manager.data.database.entities.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        ShopEntity::class,
        CustomerEntity::class,
        DebtEntity::class,
        DebtPaymentEntity::class,
        RentalEntity::class,
        RentalMachineEntity::class,
        BuyListItemEntity::class,
        TaskEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class KiranDatabase : RoomDatabase() {

    abstract fun shopDao(): ShopDao
    abstract fun customerDao(): CustomerDao
    abstract fun debtDao(): DebtDao
    abstract fun rentalDao(): RentalDao
    abstract fun buyListDao(): BuyListDao
    abstract fun taskDao(): TaskDao

    companion object {
        const val DATABASE_NAME = "kiran_store_db"

        fun create(context: Context): KiranDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                KiranDatabase::class.java,
                DATABASE_NAME
            )
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // Optional: insert seed data on first launch
                        // SeedData.populateSeedData(appDatabase)
                    }
                })
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}

// ─────────────────────────────────────────────
// SEED DATA  (call from onCreate callback or test)
// ─────────────────────────────────────────────
object SeedData {
    fun populateSeedData(db: KiranDatabase) {
        CoroutineScope(Dispatchers.IO).launch {
            // Demo shop (shopId = 1)
            val shopId = db.shopDao().insertShop(
                ShopEntity(
                    shopName = "Kiran General Store",
                    ownerName = "Vishnu",
                    phone = "9876543210",
                    address = "Vile Parle East, Mumbai",
                    userId = "demo_user"
                )
            )

            // Demo customers
            val rameshId = db.customerDao().insertCustomer(
                CustomerEntity(shopId = shopId, name = "Ramesh Kumar", phone = "+91 98765 43210")
            )
            val sunitaId = db.customerDao().insertCustomer(
                CustomerEntity(shopId = shopId, name = "Sunita Sharma", phone = "+91 88888 77777")
            )
            val amitId = db.customerDao().insertCustomer(
                CustomerEntity(shopId = shopId, name = "Amit Singh", phone = "+91 70000 12345")
            )

            // Demo debts
            db.debtDao().insertDebt(DebtEntity(customerId = rameshId, shopId = shopId, itemName = "Refined Oil (5L) + Sugar", amount = 850.0))
            db.debtDao().insertDebt(DebtEntity(customerId = rameshId, shopId = shopId, itemName = "Detergent Pack + Soap", amount = 400.0))
            db.debtDao().insertDebt(DebtEntity(customerId = sunitaId, shopId = shopId, itemName = "Rice 10kg", amount = 450.0))
            db.debtDao().insertDebt(DebtEntity(customerId = amitId, shopId = shopId, itemName = "Milk + Bread", amount = 120.0, status = DebtStatus.PAID))

            // Demo payment
            db.debtDao().insertPayment(DebtPaymentEntity(customerId = sunitaId, shopId = shopId, amount = 1000.0))

            // Demo rentals
            db.rentalDao().insertRental(RentalEntity(customerId = rameshId, shopId = shopId, machineName = "Drill Machine", deposit = 500.0, rentAmount = 200.0, status = RentalStatus.ACTIVE))
            db.rentalDao().insertRental(RentalEntity(customerId = sunitaId, shopId = shopId, machineName = "Power Grinder", deposit = 300.0, rentAmount = 150.0, status = RentalStatus.LATE))
            db.rentalDao().insertRental(RentalEntity(customerId = amitId, shopId = shopId, machineName = "Hammer Drill", deposit = 700.0, rentAmount = 350.0, status = RentalStatus.ACTIVE))

            // Demo buy list
            db.buyListDao().insertItem(BuyListItemEntity(shopId = shopId, name = "Milk Packets", quantity = "20", priority = BuyPriority.HIGH))
            db.buyListDao().insertItem(BuyListItemEntity(shopId = shopId, name = "Refined Oil", quantity = "10 L", priority = BuyPriority.HIGH))
            db.buyListDao().insertItem(BuyListItemEntity(shopId = shopId, name = "Sugar", quantity = "5 kg", priority = BuyPriority.MEDIUM))
            db.buyListDao().insertItem(BuyListItemEntity(shopId = shopId, name = "Detergent", quantity = "6 packs", priority = BuyPriority.LOW))

            // Demo tasks
            val now = System.currentTimeMillis()
            db.taskDao().insertTask(TaskEntity(shopId = shopId, name = "Open shutter at 8 AM", date = now))
            db.taskDao().insertTask(TaskEntity(shopId = shopId, name = "Call supplier for delivery", date = now))
            db.taskDao().insertTask(TaskEntity(shopId = shopId, name = "Collect Ramesh's udhaar", date = now))
            db.taskDao().insertTask(TaskEntity(shopId = shopId, name = "Check expiry dates", date = now, status = "DONE"))
            db.taskDao().insertTask(TaskEntity(shopId = shopId, name = "Stock rice shelf", date = now))
        }
    }
}
