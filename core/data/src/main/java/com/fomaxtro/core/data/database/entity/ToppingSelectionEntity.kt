package com.fomaxtro.core.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "topping_selections")
data class ToppingSelectionEntity(
    @PrimaryKey val id: Long,
    val quantity: Int,
    @ColumnInfo("cart_item_id") val cartItemId: String
)
