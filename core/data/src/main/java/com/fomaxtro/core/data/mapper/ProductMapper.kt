package com.fomaxtro.core.data.mapper

import com.fomaxtro.core.data.remote.dto.OrderProductResponse
import com.fomaxtro.core.data.remote.dto.ProductResponse
import com.fomaxtro.core.domain.model.OrderProduct
import com.fomaxtro.core.domain.model.Product

fun ProductResponse.toProduct() = Product(
    id = id,
    name = name,
    description = description,
    price = price,
    imageUrl = imageUrl,
    category = category.toProductCategory()
)

fun OrderProductResponse.toOrderProduct() = OrderProduct(
    id = id,
    name = name,
    quantity = quantity,
    unitPrice = unitPrice,
    totalPrice = totalPrice,
    toppings = toppings.map { it.toOrderTopping() }
)