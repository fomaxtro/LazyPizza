package com.fomaxtro.core.presentation.screen.history.model

object OrderProductUiFactory {
    fun create(
        id: Long,
        name: String = "Margherita",
        quantity: Int = 1
    ) = OrderProductUi(
        id = id,
        name = name,
        quantity = quantity
    )
}