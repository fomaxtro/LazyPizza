package com.fomaxtro.core.data.remote

import io.ktor.client.plugins.auth.providers.BearerTokens

interface SessionManager {
    suspend fun getBearerTokens(): BearerTokens?
    suspend fun saveBearerTokens(bearerTokens: BearerTokens)
    suspend fun clearSession()
}