package com.fomaxtro.core.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class OrderProductRequest(
    val id: Long,
    val quantity: Int,
    val toppings: List<OrderToppingRequest>
)
