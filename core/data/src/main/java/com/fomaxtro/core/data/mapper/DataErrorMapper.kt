package com.fomaxtro.core.data.mapper

import com.fomaxtro.core.data.util.NetworkError
import com.fomaxtro.core.domain.error.DataError

fun NetworkError.toDataError(): DataError {
    return when (this) {
        NetworkError.NO_INTERNET -> DataError.Network.NO_CONNECTION
        NetworkError.SERIALIZATION -> DataError.Network.UNKNOWN
        NetworkError.BAD_REQUEST -> DataError.Validation.INVALID_INPUT
        NetworkError.UNAUTHORIZED -> DataError.Network.UNAUTHORIZED
        NetworkError.NOT_FOUND -> DataError.Resource.NOT_FOUND
        NetworkError.CONFLICT -> DataError.Resource.CONFLICT
        NetworkError.TOO_MANY_REQUESTS -> DataError.Network.SERVER_UNAVAILABLE
        NetworkError.SERVER_ERROR -> DataError.Network.SERVER_UNAVAILABLE
        NetworkError.UNKNOWN -> DataError.Network.UNKNOWN
    }
}