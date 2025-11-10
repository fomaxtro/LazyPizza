package com.fomaxtro.core.data.remote.datasource

import com.fomaxtro.core.data.remote.dto.SendOtpRequest
import com.fomaxtro.core.data.remote.dto.TokensResponse
import com.fomaxtro.core.data.remote.dto.VerifyOtpRequest
import com.fomaxtro.core.data.remote.util.createRoute
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class AuthRemoteDataSource(
    private val httpClient: HttpClient
) {
    suspend fun sendOtpRequest(request: SendOtpRequest) {
        httpClient.post(createRoute("/auth/send-otp")) {
            setBody(request)
        }
    }

    suspend fun verifyOtp(request: VerifyOtpRequest): TokensResponse {
        return httpClient.post(createRoute("/auth/verify-otp")) {
            setBody(request)
        }.body()
    }
}