package com.fomaxtro.core.domain.use_case

import com.fomaxtro.core.domain.model.CartItem
import com.fomaxtro.core.domain.model.Order
import com.fomaxtro.core.domain.model.NewOrder
import com.fomaxtro.core.domain.repository.OrderRepository
import com.fomaxtro.core.domain.util.DataError
import com.fomaxtro.core.domain.util.Result
import java.time.Instant

class PlaceOrder(
    private val orderRepository: OrderRepository
) {
    suspend operator fun invoke(
        cartItems: List<CartItem>,
        pickupTime: Instant
    ): Result<Order, DataError> {
        val order = NewOrder(
            totalPrice = cartItems.sumOf { it.totalPrice },
            pickupTime = pickupTime,
            cartItems = cartItems
        )

        return orderRepository.save(order)
    }
}