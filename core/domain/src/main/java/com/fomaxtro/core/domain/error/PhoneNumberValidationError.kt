package com.fomaxtro.core.domain.error

import com.fomaxtro.core.domain.util.ValidationError

enum class PhoneNumberValidationError : ValidationError {
    EMPTY_PHONE,
    INVALID_FORMAT
}