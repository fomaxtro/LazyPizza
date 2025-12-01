package com.fomaxtro.core.domain.model

data class OrderTopping(
    val id: Long,
    val name: String,
    val quantity: Int,
    val unitPrice: Double,
    val totalPrice: Double
)
