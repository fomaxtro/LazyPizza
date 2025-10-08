package com.fomaxtro.core.presentation.model

import com.fomaxtro.core.domain.model.ProductCategory
import com.fomaxtro.core.domain.model.ProductId

data class ProductUi(
    val id: ProductId,
    val name: String,
    val description: String?,
    val price: Double,
    val imageUrl: String,
    val category: ProductCategory,
    val quantity: Int = 0
)
