package com.fomaxtro.core.presentation.mapper

import com.fomaxtro.core.domain.model.Order
import com.fomaxtro.core.presentation.screen.history.model.OrderUi

fun Order.toUi() = OrderUi(
    id = id,
    totalPrice = totalPrice,
    status = status,
    pickupTime = pickupTime,
    products = products.map { it.toUi() }
)