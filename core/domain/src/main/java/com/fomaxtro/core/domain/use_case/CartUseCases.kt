package com.fomaxtro.core.domain.use_case

data class CartUseCases(
    val addCartItem: AddCartItem,
    val removeCartItem: RemoveCartItem,
    val changeCartItemQuantity: ChangeCartItemQuantity
)
