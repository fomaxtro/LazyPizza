package com.fomaxtro.core.domain.error

import com.fomaxtro.core.domain.util.ValidationError

enum class OtpValidationError : ValidationError {
    EMPTY_OTP,
    TOO_SHORT
}