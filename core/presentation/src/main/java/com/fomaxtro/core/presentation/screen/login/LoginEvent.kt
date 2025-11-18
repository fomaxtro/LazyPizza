package com.fomaxtro.core.presentation.screen.login

import com.fomaxtro.core.presentation.ui.UiText

sealed interface LoginEvent {
    data class ShowMessage(val message: UiText) : LoginEvent
    data object NavigateToHome : LoginEvent
}