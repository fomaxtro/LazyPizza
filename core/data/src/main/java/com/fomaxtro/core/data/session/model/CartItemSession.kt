package com.fomaxtro.core.data.session.model

import kotlinx.serialization.Serializable

@Serializable
data class CartItemSession(
    val id: String,
    val productId: Long,
    val quantity: Int,
    val selectedToppings: List<ToppingSelectionSession> = emptyList()
)
