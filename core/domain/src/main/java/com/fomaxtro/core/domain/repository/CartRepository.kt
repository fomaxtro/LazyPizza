package com.fomaxtro.core.domain.repository

import com.fomaxtro.core.domain.model.CartItem
import com.fomaxtro.core.domain.model.CartItemLocal
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    suspend fun insertCartItem(item: CartItem)
    suspend fun updateCartItem(item: CartItem)
    suspend fun removeCartItem(item: CartItem)
    fun countCartItems(): Flow<Int>
    fun getCartItemsLocal(): Flow<List<CartItemLocal>>
    suspend fun insertCartItemsLocal(items: List<CartItemLocal>)
    suspend fun clearCart()
}