package com.fomaxtro.core.domain.error

import com.fomaxtro.core.domain.util.ValidationError

enum class PickupTimeError : ValidationError {
    OUT_OF_BOUNDS,
    TOO_EARLY
}