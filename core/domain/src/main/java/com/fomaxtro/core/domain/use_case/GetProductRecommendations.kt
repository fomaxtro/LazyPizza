package com.fomaxtro.core.domain.use_case

import com.fomaxtro.core.domain.model.Product
import com.fomaxtro.core.domain.model.ProductCategory
import com.fomaxtro.core.domain.repository.ProductRepository
import com.fomaxtro.core.domain.util.DataError
import com.fomaxtro.core.domain.util.Result
import com.fomaxtro.core.domain.util.map

class GetProductRecommendations(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(): Result<List<Product>, DataError> {
        val categories = setOf(
            ProductCategory.SAUCES,
            ProductCategory.DRINKS
        )

        return productRepository.getAllByCategories(categories)
            .map { it.shuffled() }
    }
}