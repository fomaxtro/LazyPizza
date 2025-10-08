package com.fomaxtro.core.presentation.model

import com.fomaxtro.core.domain.model.ToppingId

data class ToppingUi(
    val id: ToppingId,
    val name: String,
    val price: Double,
    val imageUrl: String,
    val quantity: Int = 0
)
