package com.fomaxtro.core.data.util

import com.fomaxtro.core.domain.util.Error

enum class NetworkError : Error {
    NO_INTERNET,
    SERIALIZATION,
    BAD_REQUEST,
    UNAUTHORIZED,
    NOT_FOUND,
    CONFLICT,
    TOO_MANY_REQUESTS,
    SERVER_ERROR,
    UNKNOWN
}