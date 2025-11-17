package com.fomaxtro.core.presentation.screen.history.model

import com.fomaxtro.core.domain.model.OrderStatus
import java.time.Instant

object OrderUiFactory {
    fun create(
        id: Long,
        totalPrice: Double = 9.99,
        status: OrderStatus = OrderStatus.IN_PROGRESS,
        createdAt: Instant = Instant.now()
    ) = OrderUi(
        id = id,
        totalPrice = totalPrice,
        status = status,
        createdAt = createdAt
    )
}