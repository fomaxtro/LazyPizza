package com.fomaxtro.core.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fomaxtro.core.domain.repository.CartRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class HomeViewModel(
    private val cartRepository: CartRepository
) : ViewModel() {
    private var firstLaunch = false

    private val _state = MutableStateFlow(HomeState())
    val state = _state
        .onStart {
            if (!firstLaunch) {
                loadCartItemsCount()

                firstLaunch = true
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            HomeState()
        )

    private fun loadCartItemsCount() {
        cartRepository.countCartItems()
            .onEach { count ->
                _state.update { it.copy(cartItemsCount = count) }
            }
            .launchIn(viewModelScope)
    }
}