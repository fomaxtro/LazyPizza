package com.fomaxtro.core.data.repository

import com.fomaxtro.core.data.mapper.toCartItemLocal
import com.fomaxtro.core.data.mapper.toProduct
import com.fomaxtro.core.data.mapper.toTopping
import com.fomaxtro.core.data.mapper.toToppingSelectionSession
import com.fomaxtro.core.data.remote.datasource.ProductRemoteDataSource
import com.fomaxtro.core.data.remote.datasource.ToppingRemoteDataSource
import com.fomaxtro.core.data.session.SessionStorage
import com.fomaxtro.core.data.session.model.CartItemSession
import com.fomaxtro.core.data.util.safeRemoteCall
import com.fomaxtro.core.domain.error.DataError
import com.fomaxtro.core.domain.model.CartItem
import com.fomaxtro.core.domain.model.CartItemLocal
import com.fomaxtro.core.domain.model.ToppingSelection
import com.fomaxtro.core.domain.repository.CartRepository
import com.fomaxtro.core.domain.util.Result
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.UUID

class CartRepositoryImpl(
    private val sessionStorage: SessionStorage,
    private val productDataSource: ProductRemoteDataSource,
    private val toppingDataSource: ToppingRemoteDataSource
) : CartRepository {
    override suspend fun getCartItems(): Result<List<CartItem>, DataError> = coroutineScope {
        val cartItemsSession = sessionStorage.getCartItems().first()
        val cartToppingsSession = cartItemsSession.flatMap { it.selectedToppings }

        if (cartItemsSession.isEmpty()) {
            return@coroutineScope Result.Success(emptyList())
        }

        val toppingsDeferred = async {
            if (cartToppingsSession.isNotEmpty()) {
                safeRemoteCall { toppingDataSource.fetchAll() }
            } else Result.Success(emptyList())
        }
        val productsDeferred = async {
            safeRemoteCall { productDataSource.fetchAll() }
        }

        val toppings = when (val result = toppingsDeferred.await()) {
            is Result.Error -> return@coroutineScope result
            is Result.Success -> result.data
        }
        val products = when (val result = productsDeferred.await()) {
            is Result.Error -> return@coroutineScope result
            is Result.Success -> result.data
        }

        val cartItems = cartItemsSession
            .mapNotNull { cartItem ->
                val product = products.find { it.id == cartItem.productId }
                val selectedToppings = cartItem.selectedToppings
                    .mapNotNull { selectedTopping ->
                        val topping = toppings
                            .find { it.id == selectedTopping.id }
                            ?.toTopping()

                        ToppingSelection(
                            topping = topping ?: return@mapNotNull null,
                            quantity = selectedTopping.quantity
                        )
                    }

                CartItem(
                    id = UUID.fromString(cartItem.id),
                    product = product?.toProduct() ?: return@mapNotNull null,
                    quantity = cartItem.quantity,
                    selectedToppings = selectedToppings
                )
            }

        Result.Success(cartItems)
    }

    override fun getCartItemsLocal(): Flow<List<CartItemLocal>> {
        return sessionStorage
            .getCartItems()
            .map { cartItems ->
                cartItems.map { it.toCartItemLocal() }
            }
            .distinctUntilChanged()
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

    override suspend fun upsertCartItem(item: CartItem) {
        val upsertItem = CartItemSession(
            id = item.id.toString(),
            productId = item.product.id,
            quantity = item.quantity,
            selectedToppings = item.selectedToppings.map { it.toToppingSelectionSession() }
        )

        sessionStorage.upsertCartItem(upsertItem)
    }
}