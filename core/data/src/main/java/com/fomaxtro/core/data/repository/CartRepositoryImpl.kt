package com.fomaxtro.core.data.repository

import com.fomaxtro.core.data.mapper.toCartItemSession
import com.fomaxtro.core.data.mapper.toProduct
import com.fomaxtro.core.data.mapper.toTopping
import com.fomaxtro.core.data.remote.datasource.ProductRemoteDataSource
import com.fomaxtro.core.data.remote.datasource.ToppingRemoteDataSource
import com.fomaxtro.core.data.session.SessionStorage
import com.fomaxtro.core.data.util.safeRemoteCall
import com.fomaxtro.core.domain.error.DataError
import com.fomaxtro.core.domain.model.CartItem
import com.fomaxtro.core.domain.model.ToppingSelection
import com.fomaxtro.core.domain.repository.CartRepository
import com.fomaxtro.core.domain.util.Result
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first

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

        val toppingsResult = when (val result = toppingsDeferred.await()) {
            is Result.Error -> return@coroutineScope result
            is Result.Success -> result.data
        }
        val productsResult = when (val result = productsDeferred.await()) {
            is Result.Error -> return@coroutineScope result
            is Result.Success -> result.data
        }

        val filteredToppings = toppingsResult
            .filter { topping ->
                cartToppingsSession.any { it.id == topping.id }
            }
        val filteredProducts = productsResult
            .filter { product ->
                cartItemsSession.any { it.id == product.id }
            }

        val cartItems = cartItemsSession
            .mapNotNull { cartItem ->
                val product = filteredProducts.find { it.id == cartItem.id }
                val selectedToppings = cartItem.selectedToppings
                    .mapNotNull { selectedTopping ->
                        val topping = filteredToppings
                            .find { it.id == selectedTopping.id }
                            ?.toTopping()

                        ToppingSelection(
                            topping = topping ?: return@mapNotNull null,
                            quantity = selectedTopping.quantity
                        )
                    }

                CartItem(
                    product = product?.toProduct() ?: return@mapNotNull null,
                    quantity = cartItem.quantity,
                    selectedToppings = selectedToppings
                )
            }

        Result.Success(cartItems)
    }

    override suspend fun upsertCartItem(item: CartItem) {
        sessionStorage.upsertCartItem(item.toCartItemSession())
    }

    override suspend fun removeCartItem(item: CartItem) {
        sessionStorage.removeCartItem(item.toCartItemSession())
    }
}