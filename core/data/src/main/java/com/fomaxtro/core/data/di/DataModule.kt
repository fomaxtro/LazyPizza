package com.fomaxtro.core.data.di

import com.fomaxtro.core.data.remote.di.remoteModule
import com.fomaxtro.core.data.repository.CartRepositoryImpl
import com.fomaxtro.core.data.repository.ProductRepositoryImpl
import com.fomaxtro.core.data.repository.ToppingRepositoryImpl
import com.fomaxtro.core.data.session.di.sessionModule
import com.fomaxtro.core.domain.repository.CartRepository
import com.fomaxtro.core.domain.repository.ProductRepository
import com.fomaxtro.core.domain.repository.ToppingRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule = module {
    includes(remoteModule, sessionModule)

    singleOf(::ProductRepositoryImpl).bind<ProductRepository>()
    singleOf(::ToppingRepositoryImpl).bind<ToppingRepository>()
    singleOf(::CartRepositoryImpl).bind<CartRepository>()
}