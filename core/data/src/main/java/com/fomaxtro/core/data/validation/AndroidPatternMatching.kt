package com.fomaxtro.core.data.validation

import android.util.Patterns
import com.fomaxtro.core.domain.validation.PatternMatching

class AndroidPatternMatching : PatternMatching {
    override fun isValidPhoneNumber(phoneNumber: String): Boolean {
        return Patterns.PHONE.matcher(phoneNumber).matches()
    }
}