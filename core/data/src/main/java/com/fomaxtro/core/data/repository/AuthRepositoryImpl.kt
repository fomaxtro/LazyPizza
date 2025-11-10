package com.fomaxtro.core.data.repository

import com.fomaxtro.core.data.remote.datasource.AuthRemoteDataSource
import com.fomaxtro.core.data.remote.dto.SendOtpRequest
import com.fomaxtro.core.data.session.SecureSessionStorage
import com.fomaxtro.core.data.util.safeRemoteCall
import com.fomaxtro.core.domain.repository.AuthRepository
import com.fomaxtro.core.domain.util.DataError
import com.fomaxtro.core.domain.util.EmptyResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthRepositoryImpl(
    private val secureSessionStorage: SecureSessionStorage,
    private val authRemoteDataSource: AuthRemoteDataSource
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
}