package com.fomaxtro.core.domain.error

import com.fomaxtro.core.domain.util.Error

sealed interface DataError : Error {
    enum class Network : DataError {
        NO_CONNECTION,
        SERVICE_UNAVAILABLE,
        UNAUTHORIZED,
        TOO_MANY_REQUESTS,
        UNKNOWN
    }

    enum class Resource : DataError {
        NOT_FOUND,
        CONFLICT
    }

    enum class Validation : DataError {
        INVALID_INPUT
    }
}