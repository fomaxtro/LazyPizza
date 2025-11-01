package com.fomaxtro.core.presentation.util

sealed interface Resource<out D> {
    data object Loading : Resource<Nothing>
    data class Success<D>(val data: D) : Resource<D>
    data object Error : Resource<Nothing>

    val isLoading: Boolean
        get() = this is Loading
}

fun <D, R> Resource<D>.map(transform: (D) -> R): Resource<R> {
    return when (this) {
        Resource.Error -> Resource.Error
        Resource.Loading -> Resource.Loading
        is Resource.Success -> Resource.Success(transform(data))
    }
}

fun <D> Resource<D>.getOrNull(): D? {
    return (this as? Resource.Success)?.data
}

fun <D> Resource<D>.getOrDefault(default: D): D {
    return (this as? Resource.Success)?.data ?: default
}