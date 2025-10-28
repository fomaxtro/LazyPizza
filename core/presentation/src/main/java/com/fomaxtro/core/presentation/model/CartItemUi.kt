package com.fomaxtro.core.presentation.model

data class CartItemUi(
    val id: String,
    val product: ProductUi,
    val quantity: Int = 0,
    val selectedToppings: List<ToppingSelectionUi> = emptyList()
) {
    val priceWithToppings: Double
        get() {
            val toppingsPrice = selectedToppings.sumOf { it.totalPrice }

            return product.price + toppingsPrice
        }
    val totalPrice: Double = priceWithToppings * quantity
}
