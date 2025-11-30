package com.fomaxtro.core.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class OrderRequest(
    val totalPrice: Double,
    val pickupTime: String,
    val products: List<OrderProductRequest>,
    val comments: String?
)
