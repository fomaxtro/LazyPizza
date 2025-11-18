package com.fomaxtro.core.data.repository

import com.fomaxtro.core.data.mapper.toTokenPairSession
import com.fomaxtro.core.data.remote.datasource.AuthRemoteDataSource
import com.fomaxtro.core.data.remote.dto.RefreshTokenRequest
import com.fomaxtro.core.data.remote.dto.SendOtpRequest
import com.fomaxtro.core.data.remote.dto.VerifyOtpRequest
import com.fomaxtro.core.data.session.SecureSessionStorage
import com.fomaxtro.core.data.util.safeRemoteCall
import com.fomaxtro.core.domain.repository.AuthRepository
import com.fomaxtro.core.domain.util.DataError
import com.fomaxtro.core.domain.util.EmptyResult
import com.fomaxtro.core.domain.util.Result
import com.fomaxtro.core.domain.util.asEmptyResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber

class AuthRepositoryImpl(
    private val secureSessionStorage: SecureSessionStorage,
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val applicationScope: CoroutineScope
) : AuthRepository {
    override fun isAuthenticated(): Flow<Boolean> {
        return secureSessionStorage.getTokenPair()
            .map { it != null }
    }

    override suspend fun sendOtp(phoneNumber: String): EmptyResult<DataError> {
        return safeRemoteCall {
            authRemoteDataSource.sendOtpRequest(
                SendOtpRequest(
                    phoneNumber = phoneNumber
                )
            )
        }
    }

    override suspend fun verifyOtp(phoneNumber: String, code: String): EmptyResult<DataError> {
        val result = safeRemoteCall {
            authRemoteDataSource.verifyOtp(
                VerifyOtpRequest(
                    phoneNumber = phoneNumber,
                    code = code
                )
            )
        }

        if (result is Result.Success) {
            secureSessionStorage.saveTokenPair(result.data.toTokenPairSession())
        }

        return result.asEmptyResult()
    }

    override suspend fun logout() {
        val tokenPair = secureSessionStorage.getTokenPair().first() ?: return

        secureSessionStorage.removeTokenPair()

        applicationScope.launch {
            val request = RefreshTokenRequest(
                refreshToken = tokenPair.refreshToken
            )

            val result = safeRemoteCall { authRemoteDataSource.logout(request) }

            if (result is Result.Error) {
                Timber.tag("AuthRepositoryImpl").e("Logout failed: ${result.error}")
            }
        }
    }
}