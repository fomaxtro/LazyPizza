package com.fomaxtro.core.data.remote

import com.fomaxtro.core.data.remote.dto.ProductDto
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
}