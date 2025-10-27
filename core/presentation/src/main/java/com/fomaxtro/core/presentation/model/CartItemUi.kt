package com.fomaxtro.core.presentation.model

data class CartItemUi(
    val id: String,
    val product: ProductUi,
    val quantity: Int = 0,
    val selectedToppings: List<ToppingSelectionUi> = emptyList()
) {
    val totalPrice: Double
        get() {
            val toppingsPrice = selectedToppings.sumOf { it.totalPrice }

            return if (quantity > 0) {
                quantity * (product.price + toppingsPrice)
            } else {
                product.price
            }
        }
}
