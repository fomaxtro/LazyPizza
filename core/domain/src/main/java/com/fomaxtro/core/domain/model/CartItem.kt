package com.fomaxtro.core.domain.model

import java.util.UUID

data class CartItem(
    val id: UUID = UUID.randomUUID(),
    val product: Product,
    val quantity: Int = 0,
    val selectedToppings: List<ToppingSelection> = emptyList()
) {
    val totalPrice: Double = (product.price + selectedToppings.sumOf { it.totalPrice }) * quantity
}
