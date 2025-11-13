package com.fomaxtro.core.presentation.screen.product_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fomaxtro.core.domain.model.CartItem
import com.fomaxtro.core.domain.model.Product
import com.fomaxtro.core.domain.model.ToppingSelection
import com.fomaxtro.core.domain.repository.ProductRepository
import com.fomaxtro.core.domain.repository.ToppingRepository
import com.fomaxtro.core.domain.use_case.UpsertCartItem
import com.fomaxtro.core.domain.util.Result
import com.fomaxtro.core.presentation.mapper.toUi
import com.fomaxtro.core.presentation.mapper.toUiText
import com.fomaxtro.core.presentation.util.Resource
import com.fomaxtro.core.presentation.util.getOrDefault
import com.fomaxtro.core.presentation.util.getOrNull
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.concurrent.atomics.AtomicBoolean
import kotlin.concurrent.atomics.ExperimentalAtomicApi

@OptIn(ExperimentalAtomicApi::class)
class ProductDetailsViewModel(
    private val productId: Long,
    private val productRepository: ProductRepository,
    private val toppingRepository: ToppingRepository,
    private val upsertCartItem: UpsertCartItem
) : ViewModel() {
    private val firstLaunch = AtomicBoolean(false)

    private val _state = MutableStateFlow(ProductDetailsInternalState())

    private val eventChannel = Channel<ProductDetailsEvent>()
    val events = eventChannel.receiveAsFlow()

    private suspend fun loadProduct(): Resource<Product> {
        return when (val result = productRepository.findById(productId)) {
            is Result.Error -> {
                eventChannel.send(
                    ProductDetailsEvent.ShowSystemMessage(
                        result.error.toUiText()
                    )
                )

                Resource.Error
            }

            is Result.Success -> Resource.Success(result.data)
        }
    }

    private suspend fun loadToppings(): Resource<List<ToppingSelection>> {
        return when (val result = toppingRepository.getAll()) {
            is Result.Error -> {
                eventChannel.send(
                    ProductDetailsEvent.ShowSystemMessage(
                        result.error.toUiText()
                    )
                )

                Resource.Error
            }

            is Result.Success -> {
                val toppingSelections = result.data.map {
                    ToppingSelection(
                        topping = it
                    )
                }

                Resource.Success(toppingSelections)
            }
        }
    }

    val state = _state
        .onStart {
            if (firstLaunch.compareAndSet(expectedValue = false, newValue = true)) {
                coroutineScope {
                    val products = async { loadProduct() }
                    val toppingSelections = async { loadToppings() }

                    _state.update {
                        ProductDetailsInternalState(
                            product = products.await(),
                            toppings = toppingSelections.await()
                        )
                    }
                }
            }
        }
        .map { state -> state.toUi() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            ProductDetailsState()
        )


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
        val product = _state.value.product
            .getOrNull()
            ?: return@launch
        val selectedToppings = _state.value.toppings
            .getOrDefault(emptyList())
            .filter { it.quantity > 0 }

        val cartItem = CartItem(
            product = product,
            quantity = 1,
            selectedToppings = selectedToppings
        )

        upsertCartItem(cartItem)
        eventChannel.send(ProductDetailsEvent.NavigateToCart)
    }

    private fun onToppingQuantityChange(
        toppingId: Long,
        quantity: Int
    ) {
        val toppings = _state.value.toppings
            .getOrDefault(emptyList())
            .toMutableList()
        val toppingIndex = toppings
            .indexOfFirst { it.topping.id == toppingId }
            .takeIf { it != -1 } ?: return

        toppings[toppingIndex] = toppings[toppingIndex].copy(quantity = quantity)

        _state.update {
            it.copy(
                toppings = Resource.Success(toppings.toList())
            )
        }
    }
}

private data class ProductDetailsInternalState(
    val product: Resource<Product> = Resource.Loading,
    val toppings: Resource<List<ToppingSelection>> = Resource.Loading
)

private fun ProductDetailsInternalState.toUi() = ProductDetailsState(
    product = product.getOrNull()?.toUi(),
    isToppingsLoading = toppings.isLoading,
    toppings = toppings.getOrDefault(emptyList()).map { it.toUi() }
)