package com.fomaxtro.core.presentation.mapper

import com.fomaxtro.core.domain.error.PickupTimeError
import com.fomaxtro.core.presentation.R
import com.fomaxtro.core.presentation.ui.UiText

fun PickupTimeError.toUiText(): UiText {
    return when (this) {
        PickupTimeError.OUT_OF_BOUNDS -> UiText.StringResource(R.string.pickup_time_out_of_bounds)
        PickupTimeError.TOO_EARLY -> UiText.StringResource(R.string.pickup_time_too_early)
    }
}