package com.kiranstore.manager.data.shop

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Shop(
    val id: String? = null,
    @SerialName("user_id")
    val userId: String? = null,
    @SerialName("shop_name")
    val shopName: String,
    @SerialName("owner_name")
    val ownerName: String,
    @SerialName("phone_number")
    val phoneNumber: String,
    val address: String,
    @SerialName("logo_url")
    val logoUrl: String? = null,
    @SerialName("created_at")
    val createdAt: String? = null
)
