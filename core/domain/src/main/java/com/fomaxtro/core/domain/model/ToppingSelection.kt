package com.fomaxtro.core.domain.model

data class ToppingSelection(
    val topping: Topping,
    val quantity: Int = 0
) {
    val totalPrice: Double = topping.price * quantity
}
