package com.fomaxtro.core.data

import com.fomaxtro.core.data.remote.SessionManager
import com.fomaxtro.core.data.session.SecureSessionStorage
import com.fomaxtro.core.data.session.model.TokenPairSession
import io.ktor.client.plugins.auth.providers.BearerTokens
import kotlinx.coroutines.flow.first

class SessionStorageSessionManager(
    private val secureSessionStorage: SecureSessionStorage
) : SessionManager {
    override suspend fun getBearerTokens(): BearerTokens? {
        return secureSessionStorage.getTokenPair().first()?.let { tokenPair ->
            BearerTokens(tokenPair.accessToken, tokenPair.refreshToken)
        }
    }

    override suspend fun saveBearerTokens(bearerTokens: BearerTokens) {
        secureSessionStorage.saveTokenPair(
            TokenPairSession(
                accessToken = bearerTokens.accessToken,
                refreshToken = bearerTokens.refreshToken!!
            )
        )
    }

    override suspend fun clearSession() {
        secureSessionStorage.removeTokenPair()
    }
}