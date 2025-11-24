package com.fomaxtro.core.presentation.screen.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fomaxtro.core.domain.use_case.ObserveCartItems
import com.fomaxtro.core.domain.use_case.ObserveProductRecommendations
import com.fomaxtro.core.domain.util.Result
import com.fomaxtro.core.domain.util.getOrDefault
import com.fomaxtro.core.presentation.mapper.toResource
import com.fomaxtro.core.presentation.mapper.toUi
import com.fomaxtro.core.presentation.mapper.toUiText
import com.fomaxtro.core.presentation.util.Resource
import com.fomaxtro.core.presentation.util.map
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn

class CheckoutViewModel(
    observeCartItems: ObserveCartItems,
    observeProductRecommendations: ObserveProductRecommendations
) : ViewModel() {
    private val eventChannel = Channel<CheckoutEvent>()
    val events = eventChannel.receiveAsFlow()

    private val cartItemsShared = observeCartItems()
        .onEach { cartItems ->
            if (cartItems is Result.Error) {
                eventChannel.send(
                    CheckoutEvent.ShowSystemMessage(
                        message = cartItems.error.toUiText()
                    )
                )
            }
        }
        .shareIn(
            viewModelScope,
            SharingStarted.Lazily,
            replay = 1
        )

    private val cartItems = cartItemsShared
        .map { it.toResource() }
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            Resource.Loading
        )

    private val productRecommendations = observeProductRecommendations(
        cartItems = cartItemsShared.map { it.getOrDefault(emptyList()) }
    )
        .onEach { productRecommendations ->
            if (productRecommendations is Result.Error) {
                eventChannel.send(
                    CheckoutEvent.ShowSystemMessage(
                        message = productRecommendations.error.toUiText()
                    )
                )
            }
        }
        .map { it.toResource() }
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            Resource.Loading
        )

    val state = combine(
        cartItems,
        productRecommendations
    ) { cartItems, productRecommendations ->
        CheckoutState(
            cartItems = cartItems.map { cartItems ->
                cartItems.map { it.toUi() }
            },
            productRecommendations = productRecommendations.map { productRecommendations ->
                productRecommendations.map { it.toUi() }
            }
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        CheckoutState()
    )
}