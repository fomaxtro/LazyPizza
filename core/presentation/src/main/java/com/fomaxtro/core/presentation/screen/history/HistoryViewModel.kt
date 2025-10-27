package com.fomaxtro.core.presentation.screen.history

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

class HistoryViewModel : ViewModel() {
    private val _state = MutableStateFlow(HistoryState())
    val state = _state.asStateFlow()

    private val eventChannel = Channel<HistoryEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: HistoryAction) {
    }
}