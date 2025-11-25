package com.fomaxtro.core.presentation.screen.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fomaxtro.core.domain.model.CartItem
import com.fomaxtro.core.domain.use_case.ObserveCartItems
import com.fomaxtro.core.domain.use_case.ObserveProductRecommendations
import com.fomaxtro.core.domain.use_case.UpdateCartItemQuantity
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
import kotlin.concurrent.atomics.ExperimentalAtomicApi

@OptIn(ExperimentalAtomicApi::class)
class CartViewModel(
    private val updateCartItemQuantity: UpdateCartItemQuantity,
    observeProductRecommendations: ObserveProductRecommendations,
    observeCartItems: ObserveCartItems
) : ViewModel() {
    private val eventChannel = Channel<CartEvent>()
    val events = eventChannel.receiveAsFlow()

    private val cartItemsShared = observeCartItems()
        .onEach { cartItemsResult ->
            if (cartItemsResult is Result.Error) {
                eventChannel.send(
                    CartEvent.ShowSystemMessage(
                        message = cartItemsResult.error.toUiText()
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
                    CartEvent.ShowSystemMessage(
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
        CartState(
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
        CartState()
    )

    fun onAction(action: CartAction) {
        when (action) {
            is CartAction.OnQuantityChange -> onQuantityChange(action.cartItemId, action.quantity)
            is CartAction.OnRecommendationAddClick -> onRecommendationAddClick(action.productId)
            else -> Unit
        }
    }

    private fun onQuantityChange(cartItemId: String, quantity: Int) = viewModelScope.launch {
        val cartItem = cartItems.value.getOrNull()
            ?.find { UUID.fromString(cartItemId) == it.id } ?: return@launch

        updateCartItemQuantity(cartItem.copy(quantity = quantity))
    }

    private fun onRecommendationAddClick(productId: Long) = viewModelScope.launch {
        val product = productRecommendations.value.getOrNull()
            ?.find { productId == it.id } ?: return@launch

        val cartItem = CartItem(
            product = product,
            quantity = 1
        )

        updateCartItemQuantity(cartItem)
    }
}