package com.fomaxtro.core.domain.use_case

import com.fomaxtro.core.domain.model.CartItem
import com.fomaxtro.core.domain.model.Product
import com.fomaxtro.core.domain.model.ProductCategory
import com.fomaxtro.core.domain.repository.ProductRepository
import com.fomaxtro.core.domain.util.DataError
import com.fomaxtro.core.domain.util.Result
import com.fomaxtro.core.domain.util.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow

class GetProductRecommendations(
    private val productRepository: ProductRepository
) {
    operator fun invoke(
        cartItems: Flow<List<CartItem>>
    ): Flow<Result<List<Product>, DataError>> {
        val recommendations = flow {
            val categories = setOf(
                ProductCategory.SAUCES,
                ProductCategory.DRINKS
            )

            val recommendations = productRepository.getAllByCategories(categories)

            emit(recommendations.map { it.shuffled() })
        }

        return recommendations.combine(cartItems) { recommendations, cartItems ->
            recommendations.map { recommendations ->
                recommendations.filterNot { recommendation ->
                    cartItems.any { it.product.id == recommendation.id }
                }
            }
        }
    }
}