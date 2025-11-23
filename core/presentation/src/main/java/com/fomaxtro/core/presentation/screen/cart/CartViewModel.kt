package com.fomaxtro.core.presentation.screen.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fomaxtro.core.domain.model.CartItem
import com.fomaxtro.core.domain.use_case.GetProductRecommendations
import com.fomaxtro.core.domain.use_case.ObserveCartItems
import com.fomaxtro.core.domain.use_case.UpdateCartItemQuantity
import com.fomaxtro.core.domain.util.Result
import com.fomaxtro.core.domain.util.getOrDefault
import com.fomaxtro.core.presentation.mapper.toUi
import com.fomaxtro.core.presentation.mapper.toUiText
import com.fomaxtro.core.presentation.util.Resource
import com.fomaxtro.core.presentation.util.getOrDefault
import com.fomaxtro.core.presentation.util.map
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.concurrent.atomics.ExperimentalAtomicApi

@OptIn(ExperimentalAtomicApi::class)
class CartViewModel(
    private val getProductRecommendations: GetProductRecommendations,
    private val updateCartItemQuantity: UpdateCartItemQuantity,
    observeCartItems: ObserveCartItems
) : ViewModel() {
    private val eventChannel = Channel<CartEvent>()
    val events = eventChannel.receiveAsFlow()

    private val productRecommendations = flow { emit(getProductRecommendations()) }
        .onEach { productRecommendations ->
            if (productRecommendations is Result.Error) {
                eventChannel.send(
                    CartEvent.ShowSystemMessage(
                        message = productRecommendations.error.toUiText()
                    )
                )
            }
        }
        .map { it.getOrDefault(emptyList()) }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            emptyList()
        )

    private val cartItems = observeCartItems()
        .onEach { cartItemsResult ->
            if (cartItemsResult is Result.Error) {
                eventChannel.send(
                    CartEvent.ShowSystemMessage(
                        message = cartItemsResult.error.toUiText()
                    )
                )
            }
        }
        .map { cartItemsResult ->
            when (cartItemsResult) {
                is Result.Error -> Resource.Error
                is Result.Success -> Resource.Success(cartItemsResult.data)
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            Resource.Loading
        )

    private val filteredProductRecommendations = combine(
        productRecommendations,
        cartItems
    ) { productRecommendations, cartItems ->
        cartItems.map { cartItems ->
            productRecommendations.filterNot { product ->
                cartItems.any { it.product.id == product.id }
            }
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        Resource.Loading
    )

    val state = combine(
        cartItems,
        filteredProductRecommendations
    ) { cartItems, productRecommendations ->
        CartState(
            isCartItemsLoading = cartItems.isLoading,
            cartItems = cartItems.getOrDefault(emptyList())
                .map { it.toUi() },
            isProductRecommendationsLoading = productRecommendations.isLoading,
            productRecommendations = productRecommendations.getOrDefault(emptyList())
                .map { it.toUi() }
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
            CartAction.OnBackToMenuClick -> Unit
        }
    }

    private fun onQuantityChange(cartItemId: String, quantity: Int) = viewModelScope.launch {
        val cartItem = cartItems.value.getOrDefault(emptyList())
            .find { UUID.fromString(cartItemId) == it.id } ?: return@launch

        updateCartItemQuantity(cartItem.copy(quantity = quantity))
    }

    private fun onRecommendationAddClick(productId: Long) = viewModelScope.launch {
        val cartItem = CartItem(
            product = productRecommendations.value.find { productId == it.id } ?: return@launch,
            quantity = 1
        )

        updateCartItemQuantity(cartItem)
    }
}