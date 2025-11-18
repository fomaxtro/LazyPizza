package com.fomaxtro.core.domain.model

import java.time.Instant
import java.util.UUID

data class Order(
    val id: Long,
    val userId: UUID,
    val totalPrice: Double,
    val status: OrderStatus,
    val createdAt: Instant
)
