package com.fomaxtro.core.domain.repository

import com.fomaxtro.core.domain.error.DataError
import com.fomaxtro.core.domain.model.CartItem
import com.fomaxtro.core.domain.util.Result

interface CartRepository {
    suspend fun getCartItems(): Result<List<CartItem>, DataError>
    suspend fun upsertCartItem(item: CartItem)
    suspend fun removeCartItem(item: CartItem)
}