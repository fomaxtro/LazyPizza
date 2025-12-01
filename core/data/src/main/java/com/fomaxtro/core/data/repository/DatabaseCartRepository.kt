package com.fomaxtro.core.data.repository

import com.fomaxtro.core.data.database.dao.CartItemDao
import com.fomaxtro.core.data.mapper.toCartItemEntity
import com.fomaxtro.core.data.mapper.toCartItemLocal
import com.fomaxtro.core.data.mapper.toToppingSelectionEntity
import com.fomaxtro.core.domain.model.CartItem
import com.fomaxtro.core.domain.model.CartItemLocal
import com.fomaxtro.core.domain.repository.AuthenticatedCartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DatabaseCartRepository(
    private val cartItemDao: CartItemDao
) : AuthenticatedCartRepository {
    override suspend fun insertCartItem(item: CartItem) {
        cartItemDao.insertWithToppingSelections(
            cartItem = item.toCartItemEntity(),
            toppingSelections = item.selectedToppings.map {
                it.toToppingSelectionEntity(item.id)
            }
        )
    }

    override suspend fun updateCartItem(item: CartItem) {
        cartItemDao.update(item.toCartItemEntity())
    }

    override suspend fun removeCartItem(item: CartItem) {
        cartItemDao.delete(item.toCartItemEntity())
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

    override suspend fun insertCartItemsLocal(items: List<CartItemLocal>) {
        cartItemDao.insertAllWithToppingSelections(
            cartItems = items.map { it.toCartItemEntity() },
            toppingSelections = items.flatMap { cartItem ->
                cartItem.selectedToppings.map {
                    it.toToppingSelectionEntity(cartItem.id)
                }
            }
        )
    }

    override suspend fun clearCart() {
        cartItemDao.deleteAll()
    }
}