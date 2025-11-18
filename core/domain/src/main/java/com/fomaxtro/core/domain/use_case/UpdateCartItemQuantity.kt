package com.fomaxtro.core.domain.use_case

import com.fomaxtro.core.domain.model.CartItem

class UpdateCartItemQuantity(
    private val upsertCartItem: UpsertCartItem,
    private val removeCartItem: RemoveCartItem
) {
    suspend operator fun invoke(item: CartItem) {
        if (item.quantity > 0) {
            upsertCartItem(item)
        } else {
            removeCartItem(item)
        }
    }
}