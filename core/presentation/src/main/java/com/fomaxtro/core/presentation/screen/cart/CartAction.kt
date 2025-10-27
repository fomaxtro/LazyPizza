package com.fomaxtro.core.presentation.screen.cart

sealed interface CartAction {
    data class OnQuantityChange(val cartItemId: String, val quantity: Int) : CartAction
    data class OnRecommendationAddClick(val productId: Long) : CartAction
    data object OnBackToMenuClick : CartAction
}