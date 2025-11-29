package com.fomaxtro.core.domain.model

import java.time.Instant

data class NewOrder(
    val totalPrice: Double,
    val pickupTime: Instant,
    val cartItems: List<CartItem>
)
