package com.fomaxtro.core.data.mapper

import com.fomaxtro.core.data.session.model.CartItemSession
import com.fomaxtro.core.domain.model.CartItemLocal
import java.util.UUID

fun CartItemSession.toCartItemLocal() = CartItemLocal(
    id = UUID.fromString(id),
    productId = productId,
    quantity = quantity,
    selectedToppings = selectedToppings.map { it.toToppingSelectionLocal() },
)
