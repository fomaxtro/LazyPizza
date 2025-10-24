package com.fomaxtro.core.domain.use_case

import com.fomaxtro.core.domain.model.CartItem
import com.fomaxtro.core.domain.repository.CartRepository

class UpdateCartItemQuantity(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(item: CartItem) {
        if (item.quantity > 0) {
            cartRepository.upsertCartItem(item)
        } else {
            cartRepository.removeCartItem(item)
        }
    }
}