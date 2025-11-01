package com.fomaxtro.core.presentation.screen.product_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fomaxtro.core.domain.model.CartItem
import com.fomaxtro.core.domain.model.Product
import com.fomaxtro.core.domain.model.ToppingSelection
import com.fomaxtro.core.domain.repository.CartRepository
import com.fomaxtro.core.domain.repository.ProductRepository
import com.fomaxtro.core.domain.repository.ToppingRepository
import com.fomaxtro.core.domain.util.Result
import com.fomaxtro.core.presentation.mapper.toUi
import com.fomaxtro.core.presentation.mapper.toUiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch

class ProductDetailsViewModel(
    private val productId: Long,
    private val productRepository: ProductRepository,
    private val toppingRepository: ToppingRepository,
    private val cartRepository: CartRepository
) : ViewModel() {
    private var firstLoad = false
    private lateinit var product: Product
    private val toppingSelections = MutableStateFlow<List<ToppingSelection>>(emptyList())

    private val _state = MutableStateFlow(ProductDetailsState())
    val state = _state
        .onStart {
            coroutineScope {
                if (!firstLoad) {
                    val productJob = launch { loadProduct() }
                    val toppingsJob = launch { loadToppings() }

                    joinAll(productJob, toppingsJob)

                    firstLoad = true
                }
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            ProductDetailsState()
        )

    private val eventChannel = Channel<ProductDetailsEvent>()
    val events = eventChannel.receiveAsFlow()

    private suspend fun loadProduct() {
        when (val result = productRepository.findById(productId)) {
            is Result.Error -> {
                eventChannel.send(
                    ProductDetailsEvent.ShowSystemMessage(
                        result.error.toUiText()
                    )
                )
            }

            is Result.Success -> {
                product = result.data

                _state.update {
                    it.copy(
                        product = product.toUi()
                    )
                }
            }
        }
    }

    private suspend fun loadToppings() {
        when (val result = toppingRepository.getAll()) {
            is Result.Error -> {
                eventChannel.send(
                    ProductDetailsEvent.ShowSystemMessage(
                        result.error.toUiText()
                    )
                )
            }

            is Result.Success -> {
                toppingSelections.value = result.data.map {
                    ToppingSelection(
                        topping = it
                    )
                }

                toppingSelections
                    .onEach { toppingSelections ->
                        _state.update {
                            it.copy(
                                toppings = toppingSelections.map { topping ->
                                    topping.toUi()
                                }
                            )
                        }
                    }
                    .launchIn(viewModelScope)
            }
        }

        _state.update { it.copy(isToppingsLoading = false) }
    }

    fun onAction(action: ProductDetailsAction) {
        when (action) {
            is ProductDetailsAction.OnToppingQuantityChange -> {
                onToppingQuantityChange(action.toppingId, action.quantity)
            }

            ProductDetailsAction.OnNavigateBackClick -> Unit
            ProductDetailsAction.OnAddToCartClick -> onAddToCartClick()
        }
    }

    private fun onAddToCartClick() = viewModelScope.launch {
        val cartItem = CartItem(
            product = product,
            quantity = 1,
            selectedToppings = toppingSelections.value.filter { it.quantity > 0 }
        )

        cartRepository.upsertCartItem(cartItem)
        eventChannel.send(ProductDetailsEvent.NavigateToCart)
    }

    private fun onToppingQuantityChange(
        toppingId: Long,
        quantity: Int
    ) {
        val toppings = toppingSelections.value.toMutableList()
        val toppingIndex = toppings
            .indexOfFirst { it.topping.id == toppingId }
            .takeIf { it != -1 } ?: return

        toppings[toppingIndex] = toppings[toppingIndex].copy(quantity = quantity)

        toppingSelections.value = toppings.toList()
    }
}