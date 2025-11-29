package com.fomaxtro.core.presentation.screen.cart

import com.fomaxtro.core.presentation.model.CartItemUi
import com.fomaxtro.core.presentation.model.ProductUi
import com.fomaxtro.core.presentation.ui.Resource
import com.fomaxtro.core.presentation.ui.getOrDefault

data class CartState(
    val cartItems: Resource<List<CartItemUi>> = Resource.Loading,
    val productRecommendations: Resource<List<ProductUi>> = Resource.Loading,
) {
    val totalPrice = cartItems.getOrDefault(emptyList()).sumOf { it.totalPrice }
}