package com.fomaxtro.core.domain.use_case

import com.fomaxtro.core.domain.util.DataError
import com.fomaxtro.core.domain.model.CartItem
import com.fomaxtro.core.domain.repository.CartRepository
import com.fomaxtro.core.domain.repository.ProductRepository
import com.fomaxtro.core.domain.util.Result
import com.fomaxtro.core.domain.util.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import java.util.UUID

class ObserveProductsWithCartItems(
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository
) {
    operator fun invoke(): Flow<Result<List<CartItem>, DataError>> {
        return flow { emit(productRepository.getAll()) }
            .combine(cartRepository.getCartItemsLocal()) { productsResult, cartItems ->
                productsResult.map { products ->
                    products
                        .map { product ->
                            val foundCartItem = cartItems
                                .find { product.id == it.productId }

                            CartItem(
                                id = foundCartItem?.id ?: UUID.randomUUID(),
                                product = product,
                                quantity = foundCartItem?.quantity ?: 0
                            )
                        }
                }
            }
    }
}