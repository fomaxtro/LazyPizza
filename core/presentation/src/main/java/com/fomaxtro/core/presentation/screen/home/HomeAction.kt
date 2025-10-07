package com.fomaxtro.core.presentation.screen.home

import com.fomaxtro.core.domain.model.ProductCategory

sealed interface HomeAction {
    data class OnSearchChange(val search: String) : HomeAction
    data class OnProductCategoryToggle(val category: ProductCategory) : HomeAction
}