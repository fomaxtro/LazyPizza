package com.fomaxtro.core.domain.util

sealed interface Result<out D, out E : Error> {
    data class Success<D>(val data: D) : Result<D, Nothing>
    data class Error<E : com.fomaxtro.core.domain.util.Error>(val error: E) : Result<Nothing, E>
}

fun <D, E : Error, R> Result<D, E>.map(
    transform: (D) -> R
): Result<R, E> {
    return when (this) {
        is Result.Error -> Result.Error(error)
        is Result.Success -> Result.Success(transform(data))
    }
}

fun <D, E : Error, R : Error> Result<D, E>.mapError(
    transform: (E) -> R
): Result<D, R> {
    return when (this) {
        is Result.Error -> Result.Error(transform(error))
        is Result.Success -> Result.Success(data)
    }
}