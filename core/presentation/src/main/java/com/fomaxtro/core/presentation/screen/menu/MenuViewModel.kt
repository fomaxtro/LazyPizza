package com.fomaxtro.core.presentation.screen.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fomaxtro.core.domain.model.CartItem
import com.fomaxtro.core.domain.model.ProductCategory
import com.fomaxtro.core.domain.use_case.ObserveProductsWithCartItems
import com.fomaxtro.core.domain.use_case.UpdateCartItemQuantity
import com.fomaxtro.core.domain.util.Result
import com.fomaxtro.core.presentation.R
import com.fomaxtro.core.presentation.mapper.toUi
import com.fomaxtro.core.presentation.mapper.toUiText
import com.fomaxtro.core.presentation.ui.UiText
import com.fomaxtro.core.presentation.util.Resource
import com.fomaxtro.core.presentation.util.getOrDefault
import com.fomaxtro.core.presentation.util.getOrNull
import com.fomaxtro.core.presentation.util.map
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.concurrent.atomics.ExperimentalAtomicApi

@OptIn(ExperimentalAtomicApi::class, ExperimentalCoroutinesApi::class)
class MenuViewModel(
    private val updateCartItemQuantity: UpdateCartItemQuantity,
    observeProductsWithCartItems: ObserveProductsWithCartItems
) : ViewModel() {
    private val _state = MutableStateFlow(MenuInternalState())

    private val eventChannel = Channel<MenuEvent>()
    val events = eventChannel.receiveAsFlow()

    private val cartItems = observeProductsWithCartItems()
        .onEach { cartItemsResult ->
            if (cartItemsResult is Result.Error) {
                eventChannel.send(
                    MenuEvent.ShowSystemMessage(
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

    private val filteredCartItems = combine(
        cartItems,
        _state.map { it.search }.distinctUntilChanged(),
        _state.map { it.selectedCategory }.distinctUntilChanged()
    ) { cartItems, search, selectedCategory ->
        cartItems.map { cartItems ->
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
    }.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        Resource.Loading
    )

    val state = combine(
        _state,
        filteredCartItems
    ) { state, cartItems ->
        state.toUi(cartItems)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        MenuState()
    )

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
            is MenuAction.OnCartItemAddClick -> onCartItemAddClick(action.cartItemId)
        }
    }

    private fun onCartItemAddClick(cartItemId: String) = viewModelScope.launch {
        onCartItemQuantityChange(cartItemId, 1)
        eventChannel.send(
            MenuEvent.ShowMessage(
                message = UiText.StringResource(R.string.added_to_cart)
            )
        )
    }

    private fun onCartItemQuantityChange(
        cartItemId: String,
        quantity: Int
    ) = viewModelScope.launch {
        val cartItem = cartItems.value.getOrNull()
            ?.find { UUID.fromString(cartItemId) == it.id } ?: return@launch

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

private data class MenuInternalState(
    val search: String = "",
    val selectedCategory: ProductCategory? = null
)

private fun MenuInternalState.toUi(
    cartItems: Resource<List<CartItem>>
) = MenuState(
    search = search,
    selectedCategory = selectedCategory,
    isLoading = cartItems.isLoading,
    cartItems = cartItems.getOrDefault(emptyList())
        .map { it.toUi() }
        .groupBy { it.product.category }
)