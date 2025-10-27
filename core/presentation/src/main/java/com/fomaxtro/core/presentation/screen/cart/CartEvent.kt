package com.fomaxtro.core.presentation.screen.cart

import com.fomaxtro.core.presentation.ui.UiText

sealed interface CartEvent {
    data class ShowSystemMessage(val message: UiText) : CartEvent
}