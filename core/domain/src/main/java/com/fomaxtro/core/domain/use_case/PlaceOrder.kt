package com.fomaxtro.core.domain.use_case

import com.fomaxtro.core.domain.model.CartItem
import com.fomaxtro.core.domain.model.Order
import com.fomaxtro.core.domain.model.CreateOrderParams
import com.fomaxtro.core.domain.repository.AuthenticatedCartRepository
import com.fomaxtro.core.domain.repository.OrderRepository
import com.fomaxtro.core.domain.util.DataError
import com.fomaxtro.core.domain.util.Result
import java.time.Instant

class PlaceOrder(
    private val orderRepository: OrderRepository,
    private val authenticatedCartRepository: AuthenticatedCartRepository
) {
    suspend operator fun invoke(
        cartItems: List<CartItem>,
        pickupTime: Instant,
        comments: String?
    ): Result<Order, DataError> {
        val order = CreateOrderParams(
            pickupTime = pickupTime,
            cartItems = cartItems,
            comments = comments
        )

        return orderRepository.save(order).also { result ->
            if (result is Result.Success) {
                authenticatedCartRepository.clearCart()
            }
        }
    }
}