package com.fomaxtro.core.domain.use_case

import com.fomaxtro.core.domain.util.DataError
import com.fomaxtro.core.domain.model.CartItem
import com.fomaxtro.core.domain.model.ToppingSelection
import com.fomaxtro.core.domain.repository.CartRepository
import com.fomaxtro.core.domain.repository.ProductRepository
import com.fomaxtro.core.domain.repository.ToppingRepository
import com.fomaxtro.core.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow

class ObserveCartItems(
    private val cartRepository: CartRepository,
    private val toppingRepository: ToppingRepository,
    private val productRepository: ProductRepository
) {
    operator fun invoke(): Flow<Result<List<CartItem>, DataError>> {
        val products = flow { emit(productRepository.getAll()) }
        val toppings = flow { emit(toppingRepository.getAll()) }

        return combine(
            products,
            toppings,
            cartRepository.getCartItemsLocal()
        ) { productsResult, toppingsResult, cartItems ->
            val products = when (productsResult) {
                is Result.Error -> return@combine productsResult
                is Result.Success -> productsResult.data
            }
            val toppings = when (toppingsResult) {
                is Result.Error -> return@combine toppingsResult
                is Result.Success -> toppingsResult.data
            }

            val cartItems = cartItems
                .mapNotNull { cartItem ->
                    val foundProduct = products
                        .find { cartItem.productId == it.id } ?: return@mapNotNull null
                    val selectedToppings = cartItem.selectedToppings
                        .mapNotNull { selectedTopping ->
                            val toppingFound = toppings
                                .find { selectedTopping.id == it.id } ?: return@mapNotNull null

                            ToppingSelection(
                                topping = toppingFound,
                                quantity = selectedTopping.quantity
                            )
                        }

                    CartItem(
                        id = cartItem.id,
                        product = foundProduct,
                        quantity = cartItem.quantity,
                        selectedToppings = selectedToppings
                    )
                }

            Result.Success(cartItems)
        }
    }
}