package com.fomaxtro.core.presentation.screen.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fomaxtro.core.domain.model.CartItem
import com.fomaxtro.core.domain.use_case.CartUseCases
import com.fomaxtro.core.domain.use_case.ObserveCartItems
import com.fomaxtro.core.domain.use_case.ObserveProductRecommendations
import com.fomaxtro.core.domain.util.Result
import com.fomaxtro.core.domain.util.getOrDefault
import com.fomaxtro.core.presentation.mapper.toResource
import com.fomaxtro.core.presentation.mapper.toUi
import com.fomaxtro.core.presentation.mapper.toUiText
import com.fomaxtro.core.presentation.util.Resource
import com.fomaxtro.core.presentation.util.getOrNull
import com.fomaxtro.core.presentation.util.map
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

class CheckoutViewModel(
    observeCartItems: ObserveCartItems,
    observeProductRecommendations: ObserveProductRecommendations,
    private val cartUseCases: CartUseCases
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

    fun onAction(action: CheckoutAction) {
        when (action) {
            is CheckoutAction.OnAddProductRecommendationClick -> {
                onAddProductRecommendationClick(action.productId)
            }

            is CheckoutAction.OnCartItemQuantityChange -> onCartItemQuantityChange(
                action.cartItemId,
                action.quantity
            )

            else -> Unit
        }
    }

    private fun onCartItemQuantityChange(cartItemId: String, quantity: Int) = viewModelScope.launch {
        val cartItem = cartItems.value.getOrNull()
            ?.find { it.id == UUID.fromString(cartItemId) } ?: return@launch

        cartUseCases.changeCartItemQuantity(cartItem.copy(quantity = quantity))
    }

    private fun onAddProductRecommendationClick(productId: Long) = viewModelScope.launch {
        val product = productRecommendations.value.getOrNull()
            ?.find { it.id == productId } ?: return@launch

        val cartItem = CartItem(
            product = product,
            quantity = 1
        )

        cartUseCases.addCartItem(cartItem)
    }
}