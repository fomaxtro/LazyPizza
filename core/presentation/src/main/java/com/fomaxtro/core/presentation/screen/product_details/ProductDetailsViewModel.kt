package com.fomaxtro.core.presentation.screen.product_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fomaxtro.core.domain.model.ProductId
import com.fomaxtro.core.domain.repository.ProductRepository
import com.fomaxtro.core.domain.repository.ToppingRepository
import com.fomaxtro.core.domain.util.Result
import com.fomaxtro.core.presentation.mapper.toProductUi
import com.fomaxtro.core.presentation.mapper.toToppingUi
import com.fomaxtro.core.presentation.mapper.toUiText
import com.fomaxtro.core.presentation.model.ToppingUi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch

class ProductDetailsViewModel(
    private val id: ProductId,
    private val productRepository: ProductRepository,
    private val toppingRepository: ToppingRepository
) : ViewModel() {
    private var firstLoad = false

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
        when (val result = productRepository.findById(id)) {
            is Result.Error -> {
                eventChannel.send(
                    ProductDetailsEvent.ShowSystemMessage(
                        result.error.toUiText()
                    )
                )
            }

            is Result.Success -> {
                _state.update {
                    it.copy(
                        product = result.data.toProductUi()
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
                _state.update {
                    it.copy(
                        toppings = result.data.map { topping -> topping.toToppingUi() }
                    )
                }
            }
        }

        _state.update { it.copy(isToppingsLoading = false) }
    }

    fun onAction(action: ProductDetailsAction) {
        when (action) {
            is ProductDetailsAction.OnToppingClick -> onToppingClick(action.topping)

            is ProductDetailsAction.OnToppingQuantityChange -> {
                onToppingQuantityChange(action.topping, action.quantity)
            }

            ProductDetailsAction.OnNavigateBackClick -> Unit
        }
    }

    private fun updateToppingQuantity(topping: ToppingUi, quantity: Int) {
        val toppings = state.value.toppings.toMutableList()
        val toppingIndex = toppings.indexOf(topping)

        toppings[toppingIndex] = topping.copy(quantity = quantity)

        _state.update {
            it.copy(
                toppings = toppings.toList()
            )
        }
    }

    private fun onToppingClick(topping: ToppingUi) {
        if (topping.quantity == 0) {
            updateToppingQuantity(topping, 1)
        }
    }

    private fun onToppingQuantityChange(
        topping: ToppingUi,
        quantity: Int
    ) {
        updateToppingQuantity(topping, quantity)
    }
}