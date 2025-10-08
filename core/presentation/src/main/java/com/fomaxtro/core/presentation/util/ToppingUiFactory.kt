package com.fomaxtro.core.presentation.util

import com.fomaxtro.core.presentation.model.ToppingUi

object ToppingUiFactory {
    fun create(
        id: Long,
        name: String = "Pepperoni",
        price: Double = 0.5,
        imageUrl: String = "",
        quantity: Int = 0
    ) = ToppingUi(
        id = id,
        name = name,
        price = price,
        imageUrl = imageUrl,
        quantity = quantity
    )
}