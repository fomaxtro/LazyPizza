package com.fomaxtro.core.domain.use_case

import com.fomaxtro.core.domain.model.CartItem
import com.fomaxtro.core.domain.repository.AuthRepository
import com.fomaxtro.core.domain.repository.AuthenticatedCartRepository
import com.fomaxtro.core.domain.repository.GuestCartRepository
import kotlinx.coroutines.flow.first

class UpdateCartItemQuantity(
    private val authRepository: AuthRepository,
    private val guestCartRepository: GuestCartRepository,
    private val authenticatedCartRepository: AuthenticatedCartRepository
) {
    suspend operator fun invoke(item: CartItem) {
        require(item.quantity > 0) { "Quantity must be greater than 1 to add to cart" }

        val isAuthenticated = authRepository.isAuthenticated().first()

        if (isAuthenticated) {
            authenticatedCartRepository.updateCartItem(item)
        } else {
            guestCartRepository.updateCartItem(item)
        }
    }
}