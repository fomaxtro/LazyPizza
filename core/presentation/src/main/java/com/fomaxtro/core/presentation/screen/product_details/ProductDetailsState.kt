package com.fomaxtro.core.presentation.screen.product_details

import com.fomaxtro.core.presentation.model.ProductUi
import com.fomaxtro.core.presentation.model.ToppingUi

data class ProductDetailsState(
    val product: ProductUi? = null,
    val isToppingsLoading: Boolean = true,
    val toppings: List<ToppingUi> = emptyList()
) {
    val canAddToCart: Boolean = product != null && !isToppingsLoading
    val totalPrice: Double
        get() {
            val totalToppingsPrice = toppings.sumOf { it.price * it.quantity }

            return (product?.price ?: 0.0) + totalToppingsPrice
        }
}