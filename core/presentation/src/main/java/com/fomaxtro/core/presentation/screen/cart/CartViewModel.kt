package com.fomaxtro.core.presentation.screen.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fomaxtro.core.domain.model.CartItem
import com.fomaxtro.core.domain.model.Product
import com.fomaxtro.core.domain.repository.ProductRepository
import com.fomaxtro.core.domain.use_case.ObserveCartItems
import com.fomaxtro.core.domain.use_case.UpdateCartItemQuantity
import com.fomaxtro.core.domain.util.Result
import com.fomaxtro.core.domain.util.onError
import com.fomaxtro.core.domain.util.unwrapOr
import com.fomaxtro.core.presentation.mapper.toUi
import com.fomaxtro.core.presentation.mapper.toUiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class CartViewModel(
    private val productRepository: ProductRepository,
    private val updateCartItemQuantity: UpdateCartItemQuantity,
    observeCartItems: ObserveCartItems
) : ViewModel() {
    private var firstLaunch = false
    private val productRecommendations = MutableStateFlow<List<Product>>(emptyList())

    private val _state = MutableStateFlow(CartState())
    val state = _state
        .onStart {
            if (!firstLaunch) {
                loadProductRecommendations()
                observe()

                firstLaunch = true
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            CartState()
        )

    private val cartItems = observeCartItems()
        .onEach {
            _state.update { it.copy(isCartItemsLoading = false) }
        }
        .onError { error ->
            eventChannel.send(
                CartEvent.ShowSystemMessage(
                    message = error.toUiText()
                )
            )
        }
        .unwrapOr(emptyList())
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            emptyList()
        )

    private suspend fun loadProductRecommendations() {
        when (val result = productRepository.getRecommendations()) {
            is Result.Error -> {
                eventChannel.send(
                    CartEvent.ShowSystemMessage(
                        message = result.error.toUiText()
                    )
                )
            }

            is Result.Success -> {
                productRecommendations.value = result.data
            }
        }

        _state.update { it.copy(isProductRecommendationsLoading = false) }
    }

    private fun observe() {
        cartItems
            .map { cartItems ->
                cartItems.map { it.toUi() }
            }
            .onEach { cartItems ->
                _state.update { it.copy(cartItems = cartItems) }
            }
            .launchIn(viewModelScope)

        combine(
            productRecommendations,
            cartItems
        ) { productRecommendations, cartItems ->
            productRecommendations
                .filterNot { product ->
                    cartItems.any { it.product.id == product.id }
                }
        }
            .map { products ->
                products.map { it.toUi() }
            }
            .onEach { products ->
                _state.update { it.copy(productRecommendations = products) }
            }
            .launchIn(viewModelScope)
    }

    private val eventChannel = Channel<CartEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: CartAction) {
        when (action) {
            is CartAction.OnQuantityChange -> onQuantityChange(action.cartItemId, action.quantity)
            is CartAction.OnRecommendationAddClick -> onRecommendationAddClick(action.productId)
            CartAction.OnBackToMenuClick -> Unit
        }
    }

    private fun onQuantityChange(cartItemId: String, quantity: Int) = viewModelScope.launch {
        val cartItem = cartItems.value
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