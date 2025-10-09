package com.fomaxtro.core.data.remote.datasource

import com.fomaxtro.core.data.remote.dto.ToppingDto
import com.fomaxtro.core.data.remote.util.createRoute
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class ToppingRemoteDataSource(
    private val httpClient: HttpClient
) {
    suspend fun fetchAll(): List<ToppingDto> {
        return httpClient.get(createRoute("/toppings")).body()
    }
}