package com.fomaxtro.core.presentation.screen.home

import com.fomaxtro.core.domain.model.ProductCategory
import com.fomaxtro.core.presentation.screen.home.model.ProductUi

data class HomeState(
    val selectedCategories: Set<ProductCategory> = emptySet(),
    val isLoading: Boolean = true,
    val products: Map<ProductCategory, List<ProductUi>> = emptyMap()
)