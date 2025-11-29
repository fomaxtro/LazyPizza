package com.fomaxtro.core.presentation.screen.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fomaxtro.core.domain.model.CartItem
import com.fomaxtro.core.domain.use_case.CartUseCases
import com.fomaxtro.core.domain.use_case.ObserveCartItems
import com.fomaxtro.core.domain.use_case.ObserveProductRecommendations
import com.fomaxtro.core.domain.use_case.PlaceOrder
import com.fomaxtro.core.domain.util.Result
import com.fomaxtro.core.domain.util.ValidationResult
import com.fomaxtro.core.domain.util.getOrDefault
import com.fomaxtro.core.domain.validation.PickupTimeValidator
import com.fomaxtro.core.presentation.mapper.toResource
import com.fomaxtro.core.presentation.mapper.toUi
import com.fomaxtro.core.presentation.mapper.toUiText
import com.fomaxtro.core.presentation.screen.checkout.model.PickupOption
import com.fomaxtro.core.presentation.ui.Resource
import com.fomaxtro.core.presentation.ui.UiText
import com.fomaxtro.core.presentation.ui.getOrThrow
import com.fomaxtro.core.presentation.ui.map
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.util.UUID

class CheckoutViewModel(
    observeCartItems: ObserveCartItems,
    observeProductRecommendations: ObserveProductRecommendations,
    private val pickupTimeValidator: PickupTimeValidator,
    private val cartUseCases: CartUseCases,
    private val placeOrder: PlaceOrder
) : ViewModel() {
    private val eventChannel = Channel<CheckoutEvent>()
    val events = eventChannel.receiveAsFlow()

    private val _state = MutableStateFlow(CheckoutInternalState())

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
        _state,
        cartItems,
        productRecommendations
    ) { state, cartItems, productRecommendations ->
        CheckoutState(
            pickupOption = state.pickupOption,
            isDateTimePickerDialogVisible = state.isDateTimePickerDialogVisible,
            pickupTime = state.pickupTime,
            pickupTimeError = state.pickupTimeError,
            comments = state.comments,
            cartItems = cartItems.map { cartItems ->
                cartItems.map { it.toUi() }
            },
            productRecommendations = productRecommendations.map { productRecommendations ->
                productRecommendations.map { it.toUi() }
            },
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        CheckoutState()
    )

    fun onAction(action: CheckoutAction) {
        when (action) {
            is CheckoutAction.OnAddProductRecommendationClick -> onAddProductRecommendationClick(
                productId = action.productId
            )

            is CheckoutAction.OnCartItemQuantityChange -> onCartItemQuantityChange(
                cartItemId = action.cartItemId,
                quantity = action.quantity
            )

            is CheckoutAction.OnPickupTimeOptionSelected -> onPickupTimeOptionSelected(
                pickupOption = action.pickupOption
            )

            is CheckoutAction.OnPickupDateTimeSelected -> onPickupDateTimeSelected(
                dateTime = action.dateTime
            )

            CheckoutAction.OnPickupTimeDialogDismiss -> onPickupTimeDialogDismiss()

            is CheckoutAction.OnCommentsChange -> onCommentsChange(action.comments)

            else -> Unit
        }
    }

    private fun onCommentsChange(comments: String) {
        _state.update {
            it.copy(
                comments = comments
            )
        }
    }

    private fun onPickupTimeDialogDismiss() {
        _state.update {
            it.copy(
                isDateTimePickerDialogVisible = false
            )
        }
    }

    private fun onPickupDateTimeSelected(dateTime: ZonedDateTime) {
        val pickupTime = dateTime.toInstant()

        when (val pickupTimeResult = pickupTimeValidator.validate(pickupTime)) {
            is ValidationResult.Invalid -> {
                _state.update {
                    it.copy(
                        pickupTimeError = pickupTimeResult.error.toUiText()
                    )
                }
            }

            ValidationResult.Valid -> {
                _state.update {
                    it.copy(
                        pickupTime = pickupTime,
                        isDateTimePickerDialogVisible = false,
                        pickupOption = PickupOption.SCHEDULED,
                        pickupTimeError = null
                    )
                }
            }
        }
    }

    private fun onPickupTimeOptionSelected(pickupOption: PickupOption) {
        _state.update {
            if (pickupOption == PickupOption.SCHEDULED) {
                it.copy(
                    isDateTimePickerDialogVisible = true
                )
            } else {
                it.copy(
                    pickupOption = PickupOption.EARLIEST,
                    pickupTime = Instant.now().plus(15, ChronoUnit.MINUTES)
                )
            }
        }
    }

    private fun onCartItemQuantityChange(cartItemId: String, quantity: Int) = viewModelScope.launch {
        val cartItem = cartItems.value.getOrThrow()
            .find { it.id == UUID.fromString(cartItemId) } ?: return@launch

        cartUseCases.changeCartItemQuantity(cartItem.copy(quantity = quantity))
    }

    private fun onAddProductRecommendationClick(productId: Long) = viewModelScope.launch {
        val product = productRecommendations.value.getOrThrow()
            .find { it.id == productId } ?: return@launch

        val cartItem = CartItem(
            product = product,
            quantity = 1
        )

        cartUseCases.addCartItem(cartItem)
    }
}

private data class CheckoutInternalState(
    val pickupOption: PickupOption = PickupOption.EARLIEST,
    val isDateTimePickerDialogVisible: Boolean = false,
    val pickupTime: Instant = Instant.now().plus(15, ChronoUnit.MINUTES),
    val comments: String = "",
    val pickupTimeError: UiText? = null
)