package com.fomaxtro.core.presentation.screen.cart

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

class CartViewModel : ViewModel() {
    private val _state = MutableStateFlow(CartState())
    val state = _state.asStateFlow()

    private val eventChannel = Channel<CartEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: CartAction) {
    }
}