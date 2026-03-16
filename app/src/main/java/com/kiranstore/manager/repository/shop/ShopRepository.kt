package com.kiranstore.manager.repository.shop

import com.kiranstore.manager.data.shop.Shop
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.Columns
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShopRepository @Inject constructor(
    private val postgrest: Postgrest,
    private val auth: Auth
) {

    suspend fun getShopProfile(): Shop? {
        val userId = auth.currentUserOrNull()?.id ?: return null
        val result = postgrest.from("shops")
            .select(Columns.ALL) {
                filter {
                    eq("user_id", userId)
                }
            }
            .decodeSingleOrNull<Shop>()
        return result
    }

    suspend fun createShopProfile(shop: Shop): Shop {
        val userId = auth.currentUserOrNull()?.id
            ?: throw IllegalStateException("User not logged in")
        val shopWithUser = shop.copy(userId = userId)
        return postgrest.from("shops")
            .insert(shopWithUser) {
                select(Columns.ALL)
            }
            .decodeSingle<Shop>()
    }

    suspend fun updateShopProfile(shop: Shop): Shop {
        val userId = auth.currentUserOrNull()?.id
            ?: throw IllegalStateException("User not logged in")
        return postgrest.from("shops")
            .update(shop) {
                select(Columns.ALL)
                filter {
                    eq("user_id", userId)
                }
            }
            .decodeSingle<Shop>()
    }
}
