package com.fomaxtro.core.domain.validation

import com.fomaxtro.core.domain.error.PhoneNumberValidationError
import com.fomaxtro.core.domain.util.ValidationResult

class PhoneNumberValidator(
    private val patternMatching: PatternMatching
) {
    fun validate(phoneNumber: String): ValidationResult<PhoneNumberValidationError> {
        return when {
            phoneNumber.isBlank() -> {
                ValidationResult.Invalid(PhoneNumberValidationError.EMPTY_PHONE)
            }
            !patternMatching.isValidPhoneNumber(phoneNumber) -> {
                ValidationResult.Invalid(PhoneNumberValidationError.INVALID_FORMAT)
            }

            else -> ValidationResult.Valid
        }
    }
}