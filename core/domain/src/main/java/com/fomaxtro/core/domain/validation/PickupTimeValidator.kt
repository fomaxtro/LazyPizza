package com.fomaxtro.core.domain.validation

import com.fomaxtro.core.domain.error.PickupTimeError
import com.fomaxtro.core.domain.util.ValidationResult
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit

class PickupTimeValidator {
    fun validate(pickupTime: Instant): ValidationResult<PickupTimeError> {
        val lowerBound = LocalTime.of(10, 15)
        val upperBound = LocalTime.of(21, 45)

        val pickupLocalTime = pickupTime
            .atOffset(ZoneOffset.UTC)
            .toLocalTime()

        return when {
            pickupTime < Instant.now().plus(15, ChronoUnit.MINUTES) -> {
                ValidationResult.Invalid(PickupTimeError.TOO_EARLY)
            }

            pickupLocalTime !in lowerBound..upperBound -> {
                ValidationResult.Invalid(PickupTimeError.OUT_OF_BOUNDS)
            }

            else -> ValidationResult.Valid
        }
    }
}