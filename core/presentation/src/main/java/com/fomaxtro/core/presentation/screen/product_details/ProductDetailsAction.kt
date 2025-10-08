package com.fomaxtro.core.presentation.screen.product_details

import com.fomaxtro.core.presentation.model.ToppingUi

sealed interface ProductDetailsAction {
    data class OnToppingClick(val topping: ToppingUi) : ProductDetailsAction
    data class OnToppingQuantityChange(
        val topping: ToppingUi,
        val quantity: Int
    ) : ProductDetailsAction
}