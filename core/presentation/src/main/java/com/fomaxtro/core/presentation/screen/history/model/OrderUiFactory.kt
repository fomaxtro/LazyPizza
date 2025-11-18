package com.fomaxtro.core.presentation.screen.history.model

import com.fomaxtro.core.domain.model.OrderStatus
import java.time.Instant

object OrderUiFactory {
    fun create(
        id: Long,
        totalPrice: Double = 9.99,
        status: OrderStatus = OrderStatus.IN_PROGRESS,
        createdAt: Instant = Instant.now(),
        products: String = "1 x Margherita\n2 x Pepsi\n2 x Cookies Ice Cream"
    ) = OrderUi(
        id = id,
        totalPrice = totalPrice,
        status = status,
        createdAt = createdAt,
        products = products
    )
}