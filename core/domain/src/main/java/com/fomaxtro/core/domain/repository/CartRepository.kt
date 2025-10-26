package com.fomaxtro.core.domain.repository

import com.fomaxtro.core.domain.error.DataError
import com.fomaxtro.core.domain.model.CartItem
import com.fomaxtro.core.domain.model.CartItemLocal
import com.fomaxtro.core.domain.util.Result
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface CartRepository {
    suspend fun getCartItems(): Result<List<CartItem>, DataError>
    suspend fun upsertCartItem(item: CartItem): UUID
    suspend fun removeCartItem(item: CartItem)
    fun countCartItems(): Flow<Int>
    fun getCartItemsLocal(): Flow<List<CartItemLocal>>
}