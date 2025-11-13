package com.fomaxtro.core.data.repository

import com.fomaxtro.core.data.database.dao.CartItemDao
import com.fomaxtro.core.data.mapper.toCartItemEntity
import com.fomaxtro.core.data.mapper.toCartItemLocal
import com.fomaxtro.core.domain.model.CartItem
import com.fomaxtro.core.domain.model.CartItemLocal
import com.fomaxtro.core.domain.repository.AuthenticatedCartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DatabaseCartRepository(
    private val cartItemDao: CartItemDao
) : AuthenticatedCartRepository {
    override suspend fun upsertCartItem(item: CartItem) {
        cartItemDao.upsert(item.toCartItemEntity())
    }

    override suspend fun removeCartItem(item: CartItem) {
        cartItemDao.deleteById(item.id.toString())
    }

    override fun countCartItems(): Flow<Int> {
        return cartItemDao.countCartItems()
    }

    override fun getCartItemsLocal(): Flow<List<CartItemLocal>> {
        return cartItemDao.getCartItems()
            .map { cartItems ->
                cartItems.map { it.toCartItemLocal() }
            }
    }
}