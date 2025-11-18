package com.fomaxtro.core.domain.validation

import com.fomaxtro.core.domain.error.OtpValidationError

class OtpValidator {
    fun validate(otp: String): OtpValidationError? {
        return when {
            otp.isBlank() -> OtpValidationError.EMPTY_OTP
            otp.length < 6 -> OtpValidationError.TOO_SHORT
            else -> null
        }
    }
}