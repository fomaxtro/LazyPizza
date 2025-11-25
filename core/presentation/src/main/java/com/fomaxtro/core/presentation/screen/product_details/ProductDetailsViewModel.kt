package com.fomaxtro.core.presentation.screen.product_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fomaxtro.core.domain.model.CartItem
import com.fomaxtro.core.domain.model.ToppingSelection
import com.fomaxtro.core.domain.repository.ProductRepository
import com.fomaxtro.core.domain.repository.ToppingRepository
import com.fomaxtro.core.domain.use_case.UpsertCartItem
import com.fomaxtro.core.domain.util.Result
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
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.internal.toImmutableList

class ProductDetailsViewModel(
    private val productId: Long,
    private val productRepository: ProductRepository,
    private val toppingRepository: ToppingRepository,
    private val upsertCartItem: UpsertCartItem
) : ViewModel() {
    private val eventChannel = Channel<ProductDetailsEvent>()
    val events = eventChannel.receiveAsFlow()

    private val product = flow {
        emit(Resource.Loading)

        when (val result = productRepository.findById(productId)) {
            is Result.Error -> {
                eventChannel.send(
                    ProductDetailsEvent.ShowSystemMessage(
                        result.error.toUiText()
                    )
                )

                emit(Resource.Error)
            }

            is Result.Success -> {
                emit(Resource.Success(result.data))
            }
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        Resource.Loading
    )

    private val toppings = MutableStateFlow<Resource<List<ToppingSelection>>>(Resource.Loading)

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

    init {
        loadToppings()
    }

    private fun loadToppings() = viewModelScope.launch {
        toppings.value = Resource.Loading

        when (val result = toppingRepository.getAll()) {
            is Result.Error -> {
                eventChannel.send(
                    ProductDetailsEvent.ShowSystemMessage(
                        result.error.toUiText()
                    )
                )

                toppings.value = Resource.Error
            }

            is Result.Success -> {
                val toppingSelection = result.data.map {
                    ToppingSelection(
                        topping = it
                    )
                }

                toppings.value = Resource.Success(toppingSelection)
            }
        }
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
        val product = product.value.getOrNull() ?: return@launch
        val selectedToppings = toppings.value.getOrNull()
            ?.filter { it.quantity > 0 } ?: return@launch

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
        val toppings = toppings.value.getOrNull()?.toMutableList() ?: return
        val toppingIndex = toppings
            .indexOfFirst { it.topping.id == toppingId }
            .takeIf { it != -1 } ?: return

        toppings[toppingIndex] = toppings[toppingIndex].copy(quantity = quantity)

        this.toppings.update {
            Resource.Success(toppings.toImmutableList())
        }
    }
}