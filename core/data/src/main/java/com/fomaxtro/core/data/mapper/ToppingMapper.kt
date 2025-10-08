package com.fomaxtro.core.data.mapper

import com.fomaxtro.core.data.remote.dto.ToppingDto
import com.fomaxtro.core.domain.model.Topping

fun ToppingDto.toTopping() = Topping(
    id = id,
    name = name,
    price = price,
    imageUrl = imageUrl
)