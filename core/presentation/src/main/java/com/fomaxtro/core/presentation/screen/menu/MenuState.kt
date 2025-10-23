package com.fomaxtro.core.presentation.screen.menu

import com.fomaxtro.core.domain.model.ProductCategory
import com.fomaxtro.core.presentation.model.CartItemUi

data class MenuState(
    val search: String = "",
    val selectedCategory: ProductCategory? = null,
    val isLoading: Boolean = true,
    val cartItems: Map<ProductCategory, List<CartItemUi>> = emptyMap()
)