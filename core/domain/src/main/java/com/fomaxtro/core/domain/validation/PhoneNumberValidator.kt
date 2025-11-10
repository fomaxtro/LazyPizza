package com.fomaxtro.core.domain.validation

import com.fomaxtro.core.domain.error.PhoneNumberValidationError

class PhoneNumberValidator(
    private val patternMatching: PatternMatching
) {
    fun validate(phoneNumber: String): PhoneNumberValidationError? {
        return when {
            phoneNumber.isBlank() -> PhoneNumberValidationError.EMPTY_PHONE
            !patternMatching.isValidPhoneNumber(phoneNumber) -> {
                PhoneNumberValidationError.INVALID_FORMAT
            }

            else -> null
        }
    }
}