package com.fomaxtro.core.presentation.model

import com.fomaxtro.core.domain.model.ProductCategory

data class ProductUi(
    val id: Long,
    val name: String,
    val description: String?,
    val price: Double,
    val imageUrl: String,
    val category: ProductCategory
)
