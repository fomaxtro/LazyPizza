package com.fomaxtro.core.presentation.util

import com.fomaxtro.core.domain.model.ProductCategory
import com.fomaxtro.core.presentation.model.ProductUi

object ProductUiFactory {
    fun create(
        id: Long,
        name: String = "Pizza",
        description: String? = null,
        price: Double = 10.99,
        imageUrl: String = "",
        category: ProductCategory = ProductCategory.PIZZA
    ) = ProductUi(
        id = id,
        name = name,
        description = description,
        price = price,
        imageUrl = imageUrl,
        category = category
    )
}