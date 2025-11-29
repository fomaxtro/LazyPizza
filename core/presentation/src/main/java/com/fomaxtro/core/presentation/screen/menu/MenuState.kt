package com.fomaxtro.core.presentation.screen.menu

import com.fomaxtro.core.domain.model.ProductCategory
import com.fomaxtro.core.presentation.model.CartItemUi
import com.fomaxtro.core.presentation.ui.Resource

data class MenuState(
    val search: String = "",
    val selectedCategory: ProductCategory? = null,
    val cartItems: Resource<Map<ProductCategory, List<CartItemUi>>> = Resource.Loading
)