package com.fomaxtro.core.presentation.screen.product_details

import com.fomaxtro.core.presentation.model.ProductUi
import com.fomaxtro.core.presentation.model.ToppingSelectionUi

data class ProductDetailsState(
    val product: ProductUi? = null,
    val isToppingsLoading: Boolean = true,
    val toppings: List<ToppingSelectionUi> = emptyList()
) {
    val canAddToCart: Boolean = product != null && !isToppingsLoading
    val totalPrice = (product?.price ?: 0.0) + toppings.sumOf { it.totalPrice }
}