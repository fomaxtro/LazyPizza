package com.fomaxtro.core.presentation.screen.cart

import androidx.compose.runtime.Stable
import com.fomaxtro.core.presentation.model.ProductUi

@Stable
data class CartState(
    val isProductsLoading: Boolean = true,
    val products: List<ProductUi> = emptyList(),
    val productRecommendations: List<ProductUi> = emptyList(),
) {
    val totalPrice = products.sumOf { it.quantity * it.price }
}