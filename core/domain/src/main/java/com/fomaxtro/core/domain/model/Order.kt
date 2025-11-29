package com.fomaxtro.core.domain.model

import java.time.Instant

data class Order(
    val id: Long,
    val totalPrice: Double,
    val status: OrderStatus,
    val pickupTime: Instant
)
