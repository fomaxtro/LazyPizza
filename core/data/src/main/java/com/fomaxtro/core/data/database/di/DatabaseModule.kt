package com.fomaxtro.core.data.database.di

import androidx.room.Room
import com.fomaxtro.core.data.database.LazyPizzaDatabase
import com.fomaxtro.core.data.database.dao.CartItemDao
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single<LazyPizzaDatabase> {
        Room.databaseBuilder(
            androidContext(),
            LazyPizzaDatabase::class.java,
            "lazypizza"
        ).build()
    }

    single<CartItemDao> { get<LazyPizzaDatabase>().cartItemDao() }
}