package com.fomaxtro.core.presentation.screen.history

sealed interface HistoryAction {
    data object OnSignInClick : HistoryAction
}