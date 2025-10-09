package com.fomaxtro.core.presentation.mapper

import com.fomaxtro.core.domain.error.DataError
import com.fomaxtro.core.presentation.R
import com.fomaxtro.core.presentation.ui.UiText

fun DataError.toUiText(): UiText {
    return when (this) {
        DataError.Network.NO_CONNECTION -> UiText.StringResource(R.string.no_connection)
        DataError.Network.SERVICE_UNAVAILABLE -> UiText.StringResource(R.string.service_unavailable)
        DataError.Network.UNAUTHORIZED -> UiText.StringResource(R.string.unauthorized)
        DataError.Resource.NOT_FOUND -> UiText.StringResource(R.string.resource_not_found)
        DataError.Resource.CONFLICT -> UiText.StringResource(R.string.resource_conflict)
        DataError.Validation.INVALID_INPUT -> UiText.StringResource(R.string.invalid_input)
        DataError.Network.UNKNOWN -> UiText.StringResource(R.string.unknown_error)
        DataError.Network.TOO_MANY_REQUESTS -> UiText.StringResource(R.string.too_many_requests)
    }
}