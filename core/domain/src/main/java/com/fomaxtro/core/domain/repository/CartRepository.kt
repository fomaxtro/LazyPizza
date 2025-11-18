package com.fomaxtro.core.domain.repository

import com.fomaxtro.core.domain.model.CartItem
import com.fomaxtro.core.domain.model.CartItemLocal
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    suspend fun upsertCartItem(item: CartItem)
    suspend fun removeCartItem(item: CartItem)
    fun countCartItems(): Flow<Int>
    fun getCartItemsLocal(): Flow<List<CartItemLocal>>
    suspend fun upsertCartItemsLocal(items: List<CartItemLocal>)
    suspend fun clearCart()
}