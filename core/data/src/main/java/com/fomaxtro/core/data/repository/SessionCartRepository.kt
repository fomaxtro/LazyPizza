package com.fomaxtro.core.data.repository

import com.fomaxtro.core.data.mapper.toCartItemLocal
import com.fomaxtro.core.data.mapper.toCartItemSession
import com.fomaxtro.core.data.mapper.toToppingSelectionSession
import com.fomaxtro.core.data.session.SessionStorage
import com.fomaxtro.core.data.session.model.CartItemSession
import com.fomaxtro.core.domain.model.CartItem
import com.fomaxtro.core.domain.model.CartItemLocal
import com.fomaxtro.core.domain.repository.GuestCartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class SessionCartRepository(
    private val sessionStorage: SessionStorage
) : GuestCartRepository {
    override fun getCartItemsLocal(): Flow<List<CartItemLocal>> {
        return sessionStorage
            .getCartItems()
            .map { cartItems ->
                cartItems.map { it.toCartItemLocal() }
            }
            .distinctUntilChanged()
    }

    override suspend fun insertCartItemsLocal(items: List<CartItemLocal>) {
        sessionStorage.saveCartItems(
            items = items.map { it.toCartItemSession() }
        )
    }

    override suspend fun clearCart() {
        sessionStorage.clearCartItems()
    }

    override suspend fun removeCartItem(item: CartItem) {
        sessionStorage.removeCartItem(item.id.toString())
    }

    override fun countCartItems(): Flow<Int> {
        return sessionStorage
            .getCartItems()
            .map { cartItems -> cartItems.sumOf { it.quantity } }
            .distinctUntilChanged()
    }

    override suspend fun insertCartItem(item: CartItem) {
        val cartItem = CartItemSession(
            id = item.id.toString(),
            productId = item.product.id,
            quantity = item.quantity,
            selectedToppings = item.selectedToppings.map { it.toToppingSelectionSession() }
        )

        sessionStorage.addCartItem(cartItem)
    }

    override suspend fun updateCartItem(item: CartItem) {
        sessionStorage.updateCartItem(item.toCartItemSession())
    }
}