package com.fomaxtro.core.presentation.screen.product_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fomaxtro.core.domain.model.CartItem
import com.fomaxtro.core.domain.model.ToppingSelection
import com.fomaxtro.core.domain.repository.ProductRepository
import com.fomaxtro.core.domain.repository.ToppingRepository
import com.fomaxtro.core.domain.use_case.CartUseCases
import com.fomaxtro.core.domain.util.Result
import com.fomaxtro.core.domain.util.map
import com.fomaxtro.core.presentation.mapper.toResource
import com.fomaxtro.core.presentation.mapper.toUi
import com.fomaxtro.core.presentation.mapper.toUiText
import com.fomaxtro.core.presentation.util.Resource
import com.fomaxtro.core.presentation.util.getOrNull
import com.fomaxtro.core.presentation.util.map
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductDetailsViewModel(
    private val productId: Long,
    private val productRepository: ProductRepository,
    private val toppingRepository: ToppingRepository,
    private val cartUseCases: CartUseCases
) : ViewModel() {
    private val eventChannel = Channel<ProductDetailsEvent>()
    val events = eventChannel.receiveAsFlow()

    private val product = flow { emit(productRepository.findById(productId)) }
        .onEach { result ->
            if (result is Result.Error) {
                eventChannel.send(
                    ProductDetailsEvent.ShowSystemMessage(
                        result.error.toUiText()
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

    private val quantities = MutableStateFlow<Map<Long, Int>>(emptyMap())
    private val toppings = flow { emit(toppingRepository.getAll()) }
        .onEach { result ->
            if (result is Result.Error) {
                eventChannel.send(
                    ProductDetailsEvent.ShowSystemMessage(
                        result.error.toUiText()
                    )
                )
            }
        }
        .combine(quantities) { toppings, quantities ->
            toppings.map { toppings ->
                toppings.map { topping ->
                    ToppingSelection(
                        topping = topping,
                        quantity = quantities[topping.id] ?: 0
                    )
                }
            }.toResource()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            Resource.Loading
        )

    val state = combine(
        product,
        toppings
    ) { product, toppings ->
        ProductDetailsState(
            product = product.map { it.toUi() },
            toppings = toppings.map { toppings ->
                toppings.map { it.toUi() }
            }
        )
    }
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
            is ProductDetailsAction.OnToppingAddClick -> onToppingAddClick(action.toppingId)
        }
    }

    private fun onToppingAddClick(toppingId: Long) {
        quantities.update { currentMap ->
            currentMap + (toppingId to 1)
        }
    }

    private fun onAddToCartClick() = viewModelScope.launch {
        val product = product.value.getOrNull() ?: return@launch
        val selectedToppings = toppings.value.getOrNull()
            ?.filter { it.quantity > 0 } ?: return@launch

        val cartItem = CartItem(
            product = product,
            quantity = 1,
            selectedToppings = selectedToppings
        )

        cartUseCases.addCartItem(cartItem)
        eventChannel.send(ProductDetailsEvent.NavigateToCart)
    }

    private fun onToppingQuantityChange(
        toppingId: Long,
        quantity: Int
    ) {
        quantities.update { currentMap ->
            if (quantity > 0) {
                currentMap + (toppingId to quantity)
            } else {
                currentMap - toppingId
            }
        }
    }
}