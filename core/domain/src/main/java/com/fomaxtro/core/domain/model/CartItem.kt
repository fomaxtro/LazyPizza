package com.fomaxtro.core.domain.model

data class CartItem(
    val product: Product,
    val selectedToppings: List<ToppingSelection> = emptyList(),
    val quantity: Int
)
