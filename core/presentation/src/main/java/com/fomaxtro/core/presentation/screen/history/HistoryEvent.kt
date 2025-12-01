package com.fomaxtro.core.presentation.screen.history

import com.fomaxtro.core.presentation.ui.UiText

sealed interface HistoryEvent {
    data class ShowSystemMessage(val message: UiText) : HistoryEvent
}