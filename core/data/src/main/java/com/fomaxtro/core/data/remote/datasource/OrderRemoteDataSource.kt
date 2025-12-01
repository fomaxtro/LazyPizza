package com.fomaxtro.core.data.remote.datasource

import com.fomaxtro.core.data.remote.dto.OrderRequest
import com.fomaxtro.core.data.remote.dto.OrderResponse
import com.fomaxtro.core.data.remote.util.createRoute
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class OrderRemoteDataSource(
    private val httpClient: HttpClient
) {
    suspend fun placeOrder(order: OrderRequest): OrderResponse {
        return httpClient.post(createRoute("orders")) {
            setBody(order)
        }.body()
    }

    suspend fun fetchAll(): List<OrderResponse> {
        return httpClient.get(createRoute("orders")).body()
    }
}