package com.fomaxtro.core.data.mapper

import com.fomaxtro.core.data.remote.dto.ProductDto
import com.fomaxtro.core.domain.model.Product

fun ProductDto.toProduct() = Product(
    id = id,
    name = name,
    description = description,
    price = price,
    imageUrl = imageUrl,
    category = category.toProductCategory()
)