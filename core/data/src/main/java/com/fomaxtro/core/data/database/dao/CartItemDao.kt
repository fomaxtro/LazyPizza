package com.fomaxtro.core.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.fomaxtro.core.data.database.entity.CartItemEntity
import com.fomaxtro.core.data.database.entity.CartItemWithToppingSelections
import kotlinx.coroutines.flow.Flow

@Dao
interface CartItemDao {
    @Transaction
    @Query("SELECT * FROM cart_items")
    fun getCartItems(): Flow<List<CartItemWithToppingSelections>>

    @Upsert
    suspend fun upsert(cartItem: CartItemEntity)

    @Query("DELETE FROM cart_items WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("SELECT COUNT(*) FROM cart_items")
    fun countCartItems(): Flow<Int>
}