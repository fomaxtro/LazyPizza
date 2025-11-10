package com.fomaxtro.core.data.remote

import com.fomaxtro.core.data.remote.dto.RefreshTokenRequest
import com.fomaxtro.core.data.remote.dto.TokensResponse
import com.fomaxtro.core.data.remote.util.createRoute
import com.fomaxtro.core.data.util.safeRemoteCall
import com.fomaxtro.core.domain.util.Result
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.accept
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import timber.log.Timber

class HttpClientFactory(
    private val sessionManager: SessionManager
) {
    fun create(): HttpClient {
        return HttpClient(CIO) {
            expectSuccess = true

            defaultRequest {
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
            }

            install(ContentNegotiation) {
                json(
                    json = Json {
                        ignoreUnknownKeys = true
                        prettyPrint = true
                        isLenient = true
                    }
                )
            }

            install(Logging) {
                level = LogLevel.BODY

                logger = object : Logger {
                    override fun log(message: String) {
                        Timber.tag("HttpClient").d(message)
                    }
                }
            }

            install(Auth) {
                bearer {
                    loadTokens { sessionManager.getBearerTokens() }

                    refreshTokens {
                        val bearerTokens = sessionManager.getBearerTokens()
                            ?: return@refreshTokens null

                        val response = safeRemoteCall {
                            client.post(createRoute("/auth/refresh")) {
                                setBody(
                                    RefreshTokenRequest(
                                        refreshToken = bearerTokens.refreshToken!!
                                    )
                                )

                                markAsRefreshTokenRequest()
                            }.body<TokensResponse>()
                        }

                        when (response) {
                            is Result.Error -> {
                                sessionManager.clearSession()

                                null
                            }

                            is Result.Success -> {
                                BearerTokens(
                                    accessToken = response.data.accessToken,
                                    refreshToken = response.data.refreshToken
                                ).also {
                                    sessionManager.saveBearerTokens(it)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}