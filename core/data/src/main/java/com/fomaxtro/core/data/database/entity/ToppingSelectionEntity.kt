package com.fomaxtro.core.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "topping_selections",
    foreignKeys = [
        ForeignKey(
            entity = CartItemEntity::class,
            parentColumns = ["id"],
            childColumns = ["cart_item_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["cart_item_id", "topping_id"], unique = true)
    ]
)
data class ToppingSelectionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo("topping_id") val toppingId: Long,
    val quantity: Int,
    @ColumnInfo("cart_item_id") val cartItemId: String
)
