package com.fomaxtro.core.presentation.screen.cart

import androidx.compose.runtime.Stable
import com.fomaxtro.core.presentation.model.CartItemUi
import com.fomaxtro.core.presentation.model.ProductUi

@Stable
data class CartState(
    val isCartItemsLoading: Boolean = true,
    val isProductRecommendationsLoading: Boolean = true,
    val cartItems: List<CartItemUi> = emptyList(),
    val productRecommendations: List<ProductUi> = emptyList(),
) {
    val totalPrice = cartItems.sumOf { it.totalPrice }
}