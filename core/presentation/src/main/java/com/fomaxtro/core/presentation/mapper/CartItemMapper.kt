package com.fomaxtro.core.presentation.mapper

import com.fomaxtro.core.domain.model.CartItem
import com.fomaxtro.core.presentation.model.CartItemUi

fun CartItem.toUi() = CartItemUi(
    id = id.toString(),
    product = product.toUi(),
    quantity = quantity,
    selectedToppings = selectedToppings.map { it.toUi() }
)