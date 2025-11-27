package com.fomaxtro.core.domain.use_case

import com.fomaxtro.core.domain.model.CartItem

class ChangeCartItemQuantity(
    private val updateCartItemQuantity: UpdateCartItemQuantity,
    private val removeCartItem: RemoveCartItem
) {
    suspend operator fun invoke(item: CartItem) {
        if (item.quantity <= 0) {
            removeCartItem(item)
        } else {
            updateCartItemQuantity(item)
        }
    }
}