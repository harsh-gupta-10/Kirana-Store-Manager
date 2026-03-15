package com.kiranstore.manager.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

// ─────────────────────────────────────────────
// SHOP
// ─────────────────────────────────────────────
@Entity(tableName = "shops")
data class ShopEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val shopName: String,
    val ownerName: String,
    val phone: String,
    val address: String,
    val logoUri: String? = null,
    val userId: String,          // Supabase auth uid
    val createdAt: Long = System.currentTimeMillis()
)

// ─────────────────────────────────────────────
// CUSTOMER
// ─────────────────────────────────────────────
@Entity(
    tableName = "customers",
    foreignKeys = [ForeignKey(
        entity = ShopEntity::class,
        parentColumns = ["id"],
        childColumns = ["shopId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("shopId")]
)
data class CustomerEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val shopId: Long,
    val name: String,
    val phone: String,
    val createdAt: Long = System.currentTimeMillis()
)

// ─────────────────────────────────────────────
// DEBT
// ─────────────────────────────────────────────
@Entity(
    tableName = "debts",
    foreignKeys = [
        ForeignKey(
            entity = CustomerEntity::class,
            parentColumns = ["id"],
            childColumns = ["customerId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ShopEntity::class,
            parentColumns = ["id"],
            childColumns = ["shopId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("customerId"), Index("shopId")]
)
data class DebtEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val customerId: Long,
    val shopId: Long,
    val itemName: String,
    val amount: Double,
    val date: Long = System.currentTimeMillis(),
    val notes: String = "",
    val status: String = DebtStatus.PENDING   // PENDING | PAID
)

object DebtStatus {
    const val PENDING = "PENDING"
    const val PAID = "PAID"
}

// ─────────────────────────────────────────────
// DEBT PAYMENT
// ─────────────────────────────────────────────
@Entity(
    tableName = "debt_payments",
    foreignKeys = [
        ForeignKey(
            entity = CustomerEntity::class,
            parentColumns = ["id"],
            childColumns = ["customerId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ShopEntity::class,
            parentColumns = ["id"],
            childColumns = ["shopId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("customerId"), Index("shopId")]
)
data class DebtPaymentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val customerId: Long,
    val shopId: Long,
    val amount: Double,
    val date: Long = System.currentTimeMillis(),
    val notes: String = ""
)

// ─────────────────────────────────────────────
// RENTAL
// ─────────────────────────────────────────────
@Entity(
    tableName = "rentals",
    foreignKeys = [
        ForeignKey(
            entity = CustomerEntity::class,
            parentColumns = ["id"],
            childColumns = ["customerId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ShopEntity::class,
            parentColumns = ["id"],
            childColumns = ["shopId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("customerId"), Index("shopId")]
)
data class RentalEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val customerId: Long,
    val shopId: Long,
    val machineName: String,
    val deposit: Double,
    val rentAmount: Double,
    val startDate: Long = System.currentTimeMillis(),
    val returnDate: Long? = null,
    val status: String = RentalStatus.ACTIVE  // ACTIVE | RETURNED | LATE
)

object RentalStatus {
    const val ACTIVE = "ACTIVE"
    const val RETURNED = "RETURNED"
    const val LATE = "LATE"
}

// ─────────────────────────────────────────────
// BUY LIST ITEM
// ─────────────────────────────────────────────
@Entity(
    tableName = "buy_list_items",
    foreignKeys = [ForeignKey(
        entity = ShopEntity::class,
        parentColumns = ["id"],
        childColumns = ["shopId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("shopId")]
)
data class BuyListItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val shopId: Long,
    val name: String,
    val quantity: String,
    val priority: String = BuyPriority.MEDIUM,  // HIGH | MEDIUM | LOW
    val notes: String = "",
    val isPurchased: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

object BuyPriority {
    const val HIGH = "HIGH"
    const val MEDIUM = "MEDIUM"
    const val LOW = "LOW"
}

// ─────────────────────────────────────────────
// TASK
// ─────────────────────────────────────────────
@Entity(
    tableName = "tasks",
    foreignKeys = [ForeignKey(
        entity = ShopEntity::class,
        parentColumns = ["id"],
        childColumns = ["shopId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("shopId")]
)
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val shopId: Long,
    val name: String,
    val date: Long = System.currentTimeMillis(),
    val notes: String = "",
    val status: String = TaskStatus.PENDING   // PENDING | DONE
)

object TaskStatus {
    const val PENDING = "PENDING"
    const val DONE = "DONE"
}
