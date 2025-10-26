package com.fomaxtro.core.presentation.model

data class CartItemUi(
    val id: String,
    val product: ProductUi,
    val quantity: Int = 0,
    val selectedToppings: List<ToppingSelectionUi> = emptyList()
) {
    val totalPrice: Double
        get() {
            val toppingsPrice = selectedToppings
                .sumOf { it.quantity * it.topping.price }

            return quantity * (product.price + toppingsPrice)
        }
}
