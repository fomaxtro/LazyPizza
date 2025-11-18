package com.fomaxtro.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fomaxtro.core.data.database.dao.CartItemDao
import com.fomaxtro.core.data.database.entity.CartItemEntity
import com.fomaxtro.core.data.database.entity.ToppingSelectionEntity

@Database(
    entities = [
        ToppingSelectionEntity::class,
        CartItemEntity::class
    ],
    version = 1
)
abstract class LazyPizzaDatabase : RoomDatabase() {
    abstract fun cartItemDao(): CartItemDao
}