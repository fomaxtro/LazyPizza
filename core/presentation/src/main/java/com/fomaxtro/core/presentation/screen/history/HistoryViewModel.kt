package com.fomaxtro.core.presentation.screen.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fomaxtro.core.domain.repository.AuthRepository
import com.fomaxtro.core.domain.repository.OrderRepository
import com.fomaxtro.core.domain.util.Result
import com.fomaxtro.core.presentation.mapper.toResource
import com.fomaxtro.core.presentation.mapper.toUi
import com.fomaxtro.core.presentation.mapper.toUiText
import com.fomaxtro.core.presentation.ui.Resource
import com.fomaxtro.core.presentation.ui.map
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn

class HistoryViewModel(
    authRepository: AuthRepository,
    orderRepository: OrderRepository
) : ViewModel() {
    private val eventChannel = Channel<HistoryEvent>()
    val events = eventChannel.receiveAsFlow()

    private val isAuthenticated = authRepository.isAuthenticated()
        .map { Resource.Success(it) }
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            Resource.Loading
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    private val orders = isAuthenticated
        .filter { it is Resource.Success }
        .flatMapLatest { isAuthenticated ->
            if (isAuthenticated is Resource.Success && isAuthenticated.data) {
                flow { emit(orderRepository.findAll()) }
            } else {
                emptyFlow()
            }
        }
        .onEach { result ->
            if (result is Result.Error) {
                eventChannel.send(
                    HistoryEvent.ShowSystemMessage(
                        message = result.error.toUiText()
                    )
                )
            }
        }
        .map { it.toResource() }

    val state = combine(
        isAuthenticated,
        orders
    ) { isAuthenticated, orders ->
        HistoryState(
            isAuthenticated = isAuthenticated,
            orders = orders.map { orders -> orders.map { it.toUi() } }
        )

    }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            HistoryState()
        )
}