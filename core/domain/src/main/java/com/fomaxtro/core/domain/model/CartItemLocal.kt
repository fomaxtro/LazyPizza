package com.fomaxtro.core.domain.model

import java.util.UUID

data class CartItemLocal(
    val id: UUID,
    val productId: Long,
    val quantity: Int,
    val selectedToppings: List<ToppingSelectionLocal> = emptyList()
)
