package com.fomaxtro.core.presentation.mapper

import com.fomaxtro.core.domain.model.Topping
import com.fomaxtro.core.presentation.model.ToppingUi

fun Topping.toToppingUi() = ToppingUi(
    id = id,
    name = name,
    price = price,
    imageUrl = imageUrl
)