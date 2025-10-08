package com.fomaxtro.core.presentation.screen.product_details

import com.fomaxtro.core.presentation.ui.UiText

sealed interface ProductDetailsEvent {
    data class ShowSystemMessage(val message: UiText) : ProductDetailsEvent
}