package com.fomaxtro.core.data.mapper

import com.fomaxtro.core.data.database.entity.CartItemEntity
import com.fomaxtro.core.data.database.entity.CartItemWithToppingSelections
import com.fomaxtro.core.data.session.model.CartItemSession
import com.fomaxtro.core.domain.model.CartItem
import com.fomaxtro.core.domain.model.CartItemLocal
import java.util.UUID

fun CartItemSession.toCartItemLocal() = CartItemLocal(
    id = UUID.fromString(id),
    productId = productId,
    quantity = quantity,
    selectedToppings = selectedToppings.map { it.toToppingSelectionLocal() },
)

fun CartItem.toCartItemEntity() = CartItemEntity(
    id = id.toString(),
    productId = product.id,
    quantity = quantity,
)

fun CartItemWithToppingSelections.toCartItemLocal() = CartItemLocal(
    id = UUID.fromString(cartItem.id),
    productId = cartItem.productId,
    quantity = cartItem.quantity,
    selectedToppings = toppings.map { it.toToppingSelection() },
)

fun CartItemLocal.toCartItemSession() = CartItemSession(
    id = id.toString(),
    productId = productId,
    quantity = quantity
)

fun CartItemLocal.toCartItemEntity() = CartItemEntity(
    id = id.toString(),
    productId = productId,
    quantity = quantity,
)

fun CartItem.toCartItemSession() = CartItemSession(
    id = id.toString(),
    productId = product.id,
    quantity = quantity
)