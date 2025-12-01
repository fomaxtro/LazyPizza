package com.fomaxtro.core.data.mapper

import com.fomaxtro.core.data.remote.dto.OrderRequest
import com.fomaxtro.core.data.remote.dto.OrderResponse
import com.fomaxtro.core.domain.model.CreateOrderParams
import com.fomaxtro.core.domain.model.Order
import com.fomaxtro.core.domain.model.OrderStatus
import java.time.Instant

fun CreateOrderParams.toOrderRequest() = OrderRequest(
    pickupTime = pickupTime.toString(),
    products = cartItems.map { it.toOrderProductRequest() },
    comments = comments
)

fun OrderResponse.toOrder() = Order(
    id = id,
    totalPrice = totalPrice,
    status = OrderStatus.valueOf(status.name),
    pickupTime = Instant.parse(pickupTime),
    products = products.map { it.toOrderProduct() }
)