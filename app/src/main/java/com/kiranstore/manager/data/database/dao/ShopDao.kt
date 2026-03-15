package com.kiranstore.manager.data.database.dao

import androidx.room.*
import com.kiranstore.manager.data.database.entities.ShopEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ShopDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShop(shop: ShopEntity): Long

    @Update
    suspend fun updateShop(shop: ShopEntity)

    @Query("SELECT * FROM shops WHERE userId = :userId LIMIT 1")
    fun getShopByUserId(userId: String): Flow<ShopEntity?>

    @Query("SELECT * FROM shops WHERE id = :shopId LIMIT 1")
    suspend fun getShopById(shopId: Long): ShopEntity?

    @Query("DELETE FROM shops WHERE id = :shopId")
    suspend fun deleteShop(shopId: Long)
}
