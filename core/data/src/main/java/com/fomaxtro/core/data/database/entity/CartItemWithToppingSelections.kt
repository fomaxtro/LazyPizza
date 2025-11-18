package com.fomaxtro.core.data.database.entity

import androidx.room.Embedded
import androidx.room.Relation

data class CartItemWithToppingSelections(
    @Embedded val cartItem: CartItemEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "cart_item_id"
    )
    val toppings: List<ToppingSelectionEntity>
)
