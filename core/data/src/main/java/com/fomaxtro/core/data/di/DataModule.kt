package com.fomaxtro.core.data.di

import com.fomaxtro.core.data.SessionStorageSessionManager
import com.fomaxtro.core.data.remote.SessionManager
import com.fomaxtro.core.data.remote.di.remoteModule
import com.fomaxtro.core.data.repository.AuthRepositoryImpl
import com.fomaxtro.core.data.repository.CartRepositoryImpl
import com.fomaxtro.core.data.repository.ProductRepositoryImpl
import com.fomaxtro.core.data.repository.ToppingRepositoryImpl
import com.fomaxtro.core.data.session.di.sessionModule
import com.fomaxtro.core.data.validation.AndroidPatternMatching
import com.fomaxtro.core.domain.repository.AuthRepository
import com.fomaxtro.core.domain.repository.CartRepository
import com.fomaxtro.core.domain.repository.ProductRepository
import com.fomaxtro.core.domain.repository.ToppingRepository
import com.fomaxtro.core.domain.validation.PatternMatching
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule = module {
    includes(remoteModule, sessionModule)

    singleOf(::SessionStorageSessionManager).bind<SessionManager>()

    singleOf(::ProductRepositoryImpl).bind<ProductRepository>()
    singleOf(::ToppingRepositoryImpl).bind<ToppingRepository>()
    singleOf(::CartRepositoryImpl).bind<CartRepository>()
    singleOf(::AuthRepositoryImpl).bind<AuthRepository>()

    singleOf(::AndroidPatternMatching).bind<PatternMatching>()
}