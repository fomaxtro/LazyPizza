package com.fomaxtro.core.domain.model

data class CartItem(
    val product: Product,
    val quantity: Int = 0,
    val selectedToppings: List<ToppingSelection> = emptyList()
)
