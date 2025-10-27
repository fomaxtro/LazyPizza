package com.fomaxtro.core.presentation.screen.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fomaxtro.core.domain.model.ProductCategory
import com.fomaxtro.core.domain.use_case.ObserveProductsWithCartItems
import com.fomaxtro.core.domain.use_case.UpdateCartItemQuantity
import com.fomaxtro.core.domain.util.onError
import com.fomaxtro.core.domain.util.unwrapOr
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
import kotlinx.coroutines.launch
import java.util.UUID

class MenuViewModel(
    private val updateCartItemQuantity: UpdateCartItemQuantity,
    observeProductsWithCartItems: ObserveProductsWithCartItems
) : ViewModel() {
    private var firstLoad = false

    private val _state = MutableStateFlow(MenuState())
    val state = _state
        .onStart {
            if (!firstLoad) {
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

    private val cartItems = observeProductsWithCartItems()
        .onEach {
            _state.update { it.copy(isLoading = false) }
        }
        .onError { error ->
            eventChannel.send(
                MenuEvent.ShowSystemMessage(
                    error.toUiText()
                )
            )
        }
        .unwrapOr(emptyList())
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            emptyList()
        )

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
                onCartItemQuantityChange(action.cartItemId, action.quantity)
            }

            is MenuAction.OnProductClick -> Unit
        }
    }

    private fun onCartItemQuantityChange(
        cartItemId: String,
        quantity: Int
    ) = viewModelScope.launch {
        val cartItem = cartItems.value
            .find { UUID.fromString(cartItemId) == it.id } ?: return@launch

        updateCartItemQuantity(cartItem.copy(quantity = quantity))
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