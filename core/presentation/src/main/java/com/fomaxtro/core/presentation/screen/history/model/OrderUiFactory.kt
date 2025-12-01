package com.fomaxtro.core.presentation.screen.history.model

import com.fomaxtro.core.domain.model.OrderStatus
import java.time.Instant

object OrderUiFactory {
    fun create(
        id: Long,
        totalPrice: Double = 9.99,
        status: OrderStatus = OrderStatus.IN_PROGRESS,
        pickupTime: Instant = Instant.now(),
        products: List<OrderProductUi>
    ) = OrderUi(
        id = id,
        totalPrice = totalPrice,
        status = status,
        pickupTime = pickupTime,
        products = products
    )
}