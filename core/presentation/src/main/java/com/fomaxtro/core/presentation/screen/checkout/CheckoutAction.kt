package com.fomaxtro.core.presentation.screen.checkout

sealed interface CheckoutAction {
    data class OnCartItemQuantityChange(val cartItemId: String, val quantity: Int) : CheckoutAction
    data class OnAddProductRecommendationClick(val productId: Long) : CheckoutAction
    data object OnNavigateBackClick : CheckoutAction
}