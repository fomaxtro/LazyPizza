package com.fomaxtro.core.domain.util

sealed interface ValidationResult<out E : ValidationError> {
    data object Valid : ValidationResult<Nothing>
    data class Invalid<E : ValidationError>(val error: E) : ValidationResult<E>
}