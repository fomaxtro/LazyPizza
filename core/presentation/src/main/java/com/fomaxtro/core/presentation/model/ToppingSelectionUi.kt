package com.fomaxtro.core.presentation.model

data class ToppingSelectionUi(
    val topping: ToppingUi,
    val quantity: Int = 0
) {
    val totalPrice = topping.price * quantity
}