package com.fomaxtro.core.presentation.screen.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fomaxtro.core.domain.model.CartItem
import com.fomaxtro.core.domain.model.ProductCategory
import com.fomaxtro.core.domain.repository.ProductRepository
import com.fomaxtro.core.domain.util.Result
import com.fomaxtro.core.presentation.mapper.toCartItemUi
import com.fomaxtro.core.presentation.mapper.toUiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class MenuViewModel(
    private val productRepository: ProductRepository
) : ViewModel() {
    private var firstLoad = false
    private var cartItems = MutableStateFlow(emptyList<CartItem>())

    private val _state = MutableStateFlow(MenuState())
    val state = _state
        .onStart {
            if (!firstLoad) {
                loadProducts()
                observeFilters()

                firstLoad = true
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            MenuState()
        )

    private val eventChannel = Channel<MenuEvent>()
    val events = eventChannel.receiveAsFlow()

    private suspend fun loadProducts() {
        _state.update { it.copy(isLoading = true) }

        when (val result = productRepository.getAll()) {
            is Result.Error -> {
                eventChannel.send(
                    MenuEvent.ShowSystemMessage(result.error.toUiText())
                )
            }

            is Result.Success -> {
                cartItems.value = result.data
                    .map { product ->
                        CartItem(
                            product = product
                        )
                    }
            }
        }

        _state.update { it.copy(isLoading = false) }
    }

    private fun observeFilters() {
        val search = state
            .map { it.search }
            .distinctUntilChanged()

        val selectedCategories = state
            .map { it.selectedCategory }
            .distinctUntilChanged()

        combine(
            cartItems,
            search,
            selectedCategories
        ) { cartItems, search, selectedCategory ->
            cartItems
                .filter { cartItem ->
                    if (selectedCategory != null) {
                        selectedCategory == cartItem.product.category
                    } else true
                }
                .filter { cartItem ->
                    if (search.isEmpty()) {
                        true
                    } else {
                        cartItem.product.name.contains(search, ignoreCase = true)
                    }
                }
        }
            .onEach { cartItem ->
                _state.update { state ->
                    state.copy(
                        cartItems = cartItem
                            .map { it.toCartItemUi() }
                            .groupBy { it.product.category }
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: MenuAction) {
        when (action) {
            is MenuAction.OnSearchChange -> onSearchChange(action.search)

            is MenuAction.OnProductCategoryToggle -> {
                onProductCategoryToggle(action.category)
            }

            is MenuAction.OnCartItemQuantityChange -> {
                onCartItemQuantityChange(action.productId, action.quantity)
            }

            is MenuAction.OnProductClick -> Unit
        }
    }

    private fun onCartItemQuantityChange(
        productId: Long,
        quantity: Int
    ) {
        val mutableCartItems = cartItems.value.toMutableList()
        val cartItemIndex = mutableCartItems
            .indexOfFirst { it.product.id == productId }
            .takeIf { it != -1 } ?: return

        mutableCartItems[cartItemIndex] = mutableCartItems[cartItemIndex].copy(
            quantity = quantity
        )

        cartItems.value = mutableCartItems.toList()
    }

    private fun onProductCategoryToggle(category: ProductCategory) {
        _state.update {
            it.copy(
                selectedCategory = if (it.selectedCategory != category) {
                    category
                } else null
            )
        }
    }

    private fun onSearchChange(search: String) {
        _state.update { it.copy(search = search) }
    }
}