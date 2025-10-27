package com.fomaxtro.core.presentation.screen.menu

import com.fomaxtro.core.presentation.ui.UiText

sealed interface MenuEvent {
    data class ShowSystemMessage(val message: UiText) : MenuEvent
    data class ShowMessage(val message: UiText) : MenuEvent
}