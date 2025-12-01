package com.fomaxtro.core.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class OrderResponse(
    val id: Long,
    val totalPrice: Double,
    val status: OrderStatusResponse,
    val pickupTime: String,
    val createdAt: String,
    val products: List<OrderProductResponse>
)
