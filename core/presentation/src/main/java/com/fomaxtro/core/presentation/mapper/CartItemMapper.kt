package com.fomaxtro.core.presentation.mapper

import com.fomaxtro.core.domain.model.CartItem
import com.fomaxtro.core.presentation.model.CartItemUi

fun CartItem.toCartItemUi() = CartItemUi(
    product = product.toProductUi(),
    quantity = quantity,
    selectedToppings = selectedToppings.map { it.toToppingSelectionUi() }
)