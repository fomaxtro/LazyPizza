package com.fomaxtro.core.domain.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

sealed interface Result<out D, out E : Error> {
    data class Success<D>(val data: D) : Result<D, Nothing>
    data class Error<E : com.fomaxtro.core.domain.util.Error>(val error: E) : Result<Nothing, E>
}

inline fun <D, E : Error, R> Result<D, E>.map(
    transform: (D) -> R
): Result<R, E> {
    return when (this) {
        is Result.Error -> Result.Error(error)
        is Result.Success -> Result.Success(transform(data))
    }
}

inline fun <D, E : Error, R : Error> Result<D, E>.mapError(
    transform: (E) -> R
): Result<D, R> {
    return when (this) {
        is Result.Error -> Result.Error(transform(error))
        is Result.Success -> Result.Success(data)
    }
}

fun <D, E : Error> Result<D, E>.getOrNull(): D? {
    return when (this) {
        is Result.Error -> null
        is Result.Success -> data
    }
}

fun <D, E : Error> Result<D, E>.getOrDefault(default: D): D {
    return when (this) {
        is Result.Error -> default
        is Result.Success -> data
    }
}

fun <D, E : Error> Flow<Result<D, E>>.unwrapOr(default: D): Flow<D> {
    return map {
        when (it) {
            is Result.Error -> default
            is Result.Success -> it.data
        }
    }
}

inline fun <D, E : Error> Flow<Result<D, E>>.onError(
    crossinline onError: suspend (E) -> Unit
): Flow<Result<D, E>> {
    return onEach {
        if (it is Result.Error) {
            onError(it.error)
        }
    }
}