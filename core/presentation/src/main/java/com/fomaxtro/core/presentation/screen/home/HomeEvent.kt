package com.fomaxtro.core.presentation.screen.home

import com.fomaxtro.core.presentation.ui.UiText

sealed interface HomeEvent {
    data class ShowSystemMessage(val message: UiText) : HomeEvent
}