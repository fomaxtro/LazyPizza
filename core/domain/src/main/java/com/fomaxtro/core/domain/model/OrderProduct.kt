package com.fomaxtro.core.domain.model

data class OrderProduct(
    val id: Long,
    val name: String,
    val quantity: Int,
    val unitPrice: Double,
    val totalPrice: Double,
    val toppings: List<OrderTopping>
)
