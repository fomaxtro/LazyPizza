package com.fomaxtro.core.presentation.screen.product_details

sealed interface ProductDetailsAction {
    data class OnToppingQuantityChange(
        val toppingId: Long,
        val quantity: Int
    ) : ProductDetailsAction
    data object OnNavigateBackClick : ProductDetailsAction
}