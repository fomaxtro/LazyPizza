package com.fomaxtro.core.domain.validation

interface PatternMatching {
    fun isValidPhoneNumber(phoneNumber: String): Boolean
}