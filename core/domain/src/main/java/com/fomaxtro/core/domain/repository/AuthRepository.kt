package com.fomaxtro.core.domain.repository

import com.fomaxtro.core.domain.util.DataError
import com.fomaxtro.core.domain.util.EmptyResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun isAuthenticated(): Flow<Boolean>
    suspend fun sendOtp(phoneNumber: String): EmptyResult<DataError>
}