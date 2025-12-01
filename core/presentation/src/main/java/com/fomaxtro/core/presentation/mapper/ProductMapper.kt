package com.fomaxtro.core.presentation.mapper

import com.fomaxtro.core.domain.model.OrderProduct
import com.fomaxtro.core.domain.model.Product
import com.fomaxtro.core.presentation.model.ProductUi
import com.fomaxtro.core.presentation.screen.history.model.OrderProductUi

fun Product.toUi(): ProductUi {
    return ProductUi(
        id = id,
        name = name,
        description = description,
        price = price,
        imageUrl = imageUrl,
        category = category
    )
}

fun OrderProduct.toUi() = OrderProductUi(
    id = id,
    name = name,
    quantity = quantity
)