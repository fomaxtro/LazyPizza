package com.fomaxtro.core.domain.model

typealias ToppingId = Long

data class Topping(
    val id: ToppingId,
    val name: String,
    val price: Double,
    val imageUrl: String
)
