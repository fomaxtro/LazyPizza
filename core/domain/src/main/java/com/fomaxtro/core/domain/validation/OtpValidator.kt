package com.fomaxtro.core.domain.validation

import com.fomaxtro.core.domain.error.OtpValidationError
import com.fomaxtro.core.domain.util.ValidationResult

class OtpValidator {
    fun validate(otp: String): ValidationResult<OtpValidationError> {
        return when {
            otp.isBlank() -> ValidationResult.Invalid(OtpValidationError.EMPTY_OTP)
            otp.length < 6 -> ValidationResult.Invalid(OtpValidationError.TOO_SHORT)
            else -> ValidationResult.Valid
        }
    }
}