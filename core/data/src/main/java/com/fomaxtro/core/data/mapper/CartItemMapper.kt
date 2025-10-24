package com.fomaxtro.core.data.mapper

import com.fomaxtro.core.data.session.model.CartItemSession
import com.fomaxtro.core.domain.model.CartItem

fun CartItem.toCartItemSession() = CartItemSession(
    id = product.id,
    quantity = quantity,
    selectedToppings = selectedToppings.map { it.toToppingSelectionSession() }
)