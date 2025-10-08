package com.fomaxtro.core.data.remote

import com.fomaxtro.core.data.remote.dto.ProductDto
import com.fomaxtro.core.data.remote.dto.ToppingDto
import com.fomaxtro.core.data.remote.util.createRoute
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class RemoteDataSource(
    private val httpClient: HttpClient
) {
    suspend fun fetchAllProducts(): List<ProductDto> {
        return httpClient.get(createRoute("/products")) {
            parameter("with", "category")
        }.body()
    }

    suspend fun findProductById(id: Long): ProductDto {
        return httpClient.get(createRoute("/products/$id")) {
            parameter("with", "category")
        }.body()
    }

    suspend fun fetchAllToppings(): List<ToppingDto> {
        return httpClient.get(createRoute("/toppings")).body()
    }
}