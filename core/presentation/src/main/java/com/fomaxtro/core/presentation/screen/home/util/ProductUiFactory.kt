package com.fomaxtro.core.presentation.screen.home.util

import com.fomaxtro.core.domain.model.ProductCategory
import com.fomaxtro.core.presentation.screen.home.model.ProductUi

object ProductUiFactory {
    fun create(
        id: Long,
        name: String = "Pizza",
        description: String? = null,
        price: Double = 10.99,
        imageUrl: String = "",
        category: ProductCategory = ProductCategory.PIZZA,
        quantity: Int = 0
    ) = ProductUi(
        id = id,
        name = name,
        description = description,
        price = price,
        imageUrl = imageUrl,
        category = category,
        quantity = quantity
    )
}