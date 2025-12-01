package com.fomaxtro.core.data.remote.datasource

import com.fomaxtro.core.data.remote.dto.ProductResponse
import com.fomaxtro.core.data.remote.util.createRoute
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class ProductRemoteDataSource(
    private val httpClient: HttpClient
) {
    suspend fun fetchAll(): List<ProductResponse> {
        return httpClient.get(createRoute("/products")) {
            parameter("with", "category")
        }.body()
    }

    suspend fun findById(id: Long): ProductResponse {
        return httpClient.get(createRoute("/products/$id")) {
            parameter("with", "category")
        }.body()
    }

    suspend fun fetchAllByCategories(categoryIds: List<Long>): List<ProductResponse> {
        return httpClient.get(createRoute("/products")) {
            parameter("with", "category")
            parameter("categoryIds", categoryIds.joinToString(","))
        }.body()
    }
}