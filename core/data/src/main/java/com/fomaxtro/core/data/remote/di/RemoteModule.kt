package com.fomaxtro.core.data.remote.di

import com.fomaxtro.core.data.remote.HttpClientFactory
import com.fomaxtro.core.data.remote.datasource.ProductRemoteDataSource
import com.fomaxtro.core.data.remote.datasource.ToppingRemoteDataSource
import io.ktor.client.HttpClient
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val remoteModule = module {
    single<HttpClient> { HttpClientFactory.create() }
    singleOf(::ProductRemoteDataSource)
    singleOf(::ToppingRemoteDataSource)
}