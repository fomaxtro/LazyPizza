package com.fomaxtro.core.presentation.screen.menu

import com.fomaxtro.core.domain.model.ProductCategory
import com.fomaxtro.core.presentation.model.ProductUi

sealed interface MenuAction {
    data class OnSearchChange(val search: String) : MenuAction
    data class OnProductCategoryToggle(val category: ProductCategory) : MenuAction
    data class OnCartItemQuantityChange(val cartItemId: String, val quantity: Int) : MenuAction
    data class OnProductClick(val product: ProductUi) : MenuAction
    data class OnCartItemAddClick(val cartItemId: String) : MenuAction
    data class OnCartItemDeleteClick(val cartItemId: String) : MenuAction
}