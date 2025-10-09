package com.fomaxtro.core.presentation.screen.home

import com.fomaxtro.core.domain.model.ProductCategory
import com.fomaxtro.core.presentation.model.ProductUi

sealed interface HomeAction {
    data class OnSearchChange(val search: String) : HomeAction
    data class OnProductCategoryToggle(val category: ProductCategory) : HomeAction
    data class OnProductQuantityChange(val product: ProductUi, val quantity: Int) : HomeAction
    data class OnProductClick(val product: ProductUi) : HomeAction
}