package com.fomaxtro.core.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
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

    @Upsert
    suspend fun upsertAll(cartItems: List<CartItemEntity>)

    @Delete
    suspend fun delete(cartItem: CartItemEntity)

    @Query("SELECT SUM(quantity) FROM cart_items")
    fun countCartItems(): Flow<Int>

    @Query("DELETE FROM cart_items")
    suspend fun deleteAll()
}