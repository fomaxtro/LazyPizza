package com.fomaxtro.core.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class OrderToppingResponse(
    val id: Long,
    val name: String,
    val quantity: Int,
    val unitPrice: Double,
    val totalPrice: Double
)
