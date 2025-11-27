package com.fomaxtro.core.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.fomaxtro.core.data.database.entity.CartItemEntity
import com.fomaxtro.core.data.database.entity.CartItemWithToppingSelections
import com.fomaxtro.core.data.database.entity.ToppingSelectionEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class CartItemDao {
    @Transaction
    @Query("SELECT * FROM cart_items")
    abstract fun getCartItems(): Flow<List<CartItemWithToppingSelections>>

    @Insert
    abstract suspend fun insert(cartItem: CartItemEntity)

    @Insert
    abstract suspend fun insertAll(cartItems: List<CartItemEntity>)

    @Update
    abstract suspend fun update(cartItem: CartItemEntity)

    @Delete
    abstract suspend fun delete(cartItem: CartItemEntity)

    @Query("DELETE FROM cart_items")
    abstract suspend fun deleteAll()

    @Query("SELECT SUM(quantity) FROM cart_items")
    abstract fun countCartItems(): Flow<Int>

    @Insert
    abstract suspend fun insertAllToppingSelection(toppingSelection: List<ToppingSelectionEntity>)

    @Transaction
    open suspend fun insertWithToppingSelections(
        cartItem: CartItemEntity,
        toppingSelections: List<ToppingSelectionEntity>
    ) {
        insert(cartItem)
        insertAllToppingSelection(toppingSelections)
    }

    @Transaction
    open suspend fun insertAllWithToppingSelections(
        cartItems: List<CartItemEntity>,
        toppingSelections: List<ToppingSelectionEntity>
    ) {
        insertAll(cartItems)
        insertAllToppingSelection(toppingSelections)
    }
}