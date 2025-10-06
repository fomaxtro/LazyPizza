package com.fomaxtro.core.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fomaxtro.core.domain.repository.ProductRepository
import com.fomaxtro.core.domain.util.Result
import com.fomaxtro.core.presentation.mapper.toUiText
import com.fomaxtro.core.presentation.screen.home.mapper.toProductUi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class HomeViewModel(
    private val productRepository: ProductRepository
) : ViewModel() {
    private var firstLoad = false

    private val _state = MutableStateFlow(HomeState())
    val state = _state
        .onStart {
            if (!firstLoad) {
                loadProducts()

                firstLoad = true
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            HomeState()
        )

    private val eventChannel = Channel<HomeEvent>()
    val events = eventChannel.receiveAsFlow()

    private suspend fun loadProducts() {
        _state.update { it.copy(isLoading = true) }

        val result = productRepository.getAllProducts()

        _state.update { it.copy(isLoading = false) }


        when (result) {
            is Result.Error -> {
                eventChannel.send(
                    HomeEvent.ShowSystemMessage(result.error.toUiText())
                )
            }

            is Result.Success -> {
                _state.update { state ->
                    state.copy(
                        products = result.data
                            .map { it.toProductUi() }
                            .groupBy { it.category }
                    )
                }
            }
        }
    }

    fun onAction(action: HomeAction) {
    }
}