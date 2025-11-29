package com.fomaxtro.core.presentation.screen.product_details

import com.fomaxtro.core.presentation.model.ProductUi
import com.fomaxtro.core.presentation.model.ToppingSelectionUi
import com.fomaxtro.core.presentation.ui.Resource
import com.fomaxtro.core.presentation.ui.getOrDefault
import com.fomaxtro.core.presentation.ui.getOrNull

data class ProductDetailsState(
    val product: Resource<ProductUi> = Resource.Loading,
    val toppings: Resource<List<ToppingSelectionUi>> = Resource.Loading
) {
    val canAddToCart: Boolean = product is Resource.Success && toppings !is Resource.Loading
    val totalPrice: Double = product.getOrNull()?.let { product ->
        product.price + toppings.getOrDefault(emptyList()).sumOf { it.totalPrice }
    } ?: 0.0
}