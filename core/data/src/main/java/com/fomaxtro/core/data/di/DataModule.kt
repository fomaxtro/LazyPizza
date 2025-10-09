package com.fomaxtro.core.data.di

import com.fomaxtro.core.data.remote.HttpClientFactory
import com.fomaxtro.core.data.remote.datasource.ProductRemoteDataSource
import com.fomaxtro.core.data.remote.datasource.ToppingRemoteDataSource
import com.fomaxtro.core.data.repository.ProductRepositoryImpl
import com.fomaxtro.core.data.repository.ToppingRepositoryImpl
import com.fomaxtro.core.domain.repository.ProductRepository
import com.fomaxtro.core.domain.repository.ToppingRepository
import io.ktor.client.HttpClient
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule = module {
    single<HttpClient> { HttpClientFactory.create() }
    singleOf(::ProductRemoteDataSource)
    singleOf(::ToppingRemoteDataSource)
    singleOf(::ProductRepositoryImpl).bind<ProductRepository>()
    singleOf(::ToppingRepositoryImpl).bind<ToppingRepository>()
}