package com.fomaxtro.core.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey val id: String,
    @ColumnInfo("product_id") val productId: Long,
    val quantity: Int
)
