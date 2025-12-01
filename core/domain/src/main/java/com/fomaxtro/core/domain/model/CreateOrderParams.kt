package com.fomaxtro.core.domain.model

import java.time.Instant

data class CreateOrderParams(
    val pickupTime: Instant,
    val cartItems: List<CartItem>,
    val comments: String?
)
