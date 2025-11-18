package com.fomaxtro.core.data.session

import androidx.datastore.core.DataStore
import com.fomaxtro.core.data.session.model.TokenPairSession
import com.fomaxtro.core.data.session.secure.SecureSessionData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class SecureSessionStorage(
    private val dataStore: DataStore<SecureSessionData>
) {
    suspend fun saveTokenPair(tokenPair: TokenPairSession) {
        dataStore.updateData { data ->
            data.copy(
                tokenPair = tokenPair
            )
        }
    }

    fun getTokenPair(): Flow<TokenPairSession?> {
        return dataStore.data
            .map { it.tokenPair }
            .distinctUntilChanged()
    }

    suspend fun removeTokenPair() {
        dataStore.updateData { data ->
            data.copy(
                tokenPair = null
            )
        }
    }
}