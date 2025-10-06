package com.fomaxtro.core.presentation.screen.home.model

import com.fomaxtro.core.domain.model.ProductCategory

data class ProductUi(
    val id: Long,
    val name: String,
    val description: String?,
    val price: Double,
    val imagUrl: String,
    val category: ProductCategory,
    val quantity: Int = 0
)
