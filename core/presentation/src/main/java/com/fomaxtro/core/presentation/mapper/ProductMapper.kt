package com.fomaxtro.core.presentation.mapper

import com.fomaxtro.core.domain.model.Product
import com.fomaxtro.core.presentation.model.ProductUi

fun Product.toProductUi() = ProductUi(
    id = id,
    name = name,
    description = description,
    price = price,
    imageUrl = imageUrl,
    category = category
)