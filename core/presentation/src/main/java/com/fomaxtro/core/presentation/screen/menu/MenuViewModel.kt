package com.fomaxtro.core.presentation.screen.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fomaxtro.core.domain.model.ProductCategory
import com.fomaxtro.core.domain.repository.ProductRepository
import com.fomaxtro.core.domain.util.Result
import com.fomaxtro.core.presentation.mapper.toProductUi
import com.fomaxtro.core.presentation.mapper.toUiText
import com.fomaxtro.core.presentation.model.ProductUi
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
    private var products = emptyList<ProductUi>()

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
                _state.update { state ->
                    state.copy(
                        products = result.data
                            .map { it.toProductUi() }
                            .also { products = it }
                            .groupBy { it.category }
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

        combine(search, selectedCategories) { search, selectedCategory ->
            products
                .filter { product ->
                    if (selectedCategory != null) {
                        selectedCategory == product.category
                    } else true
                }
                .filter { product ->
                    if (search.isEmpty()) {
                        true
                    } else {
                        product.name.contains(search, ignoreCase = true)
                    }
                }
        }
            .onEach { products ->
                _state.update { state ->
                    state.copy(
                        products = products.groupBy { it.category }
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

            is MenuAction.OnProductQuantityChange -> {
                onProductQuantityChange(action.product, action.quantity)
            }

            is MenuAction.OnProductClick -> Unit
        }
    }

    private fun onProductQuantityChange(
        product: ProductUi,
        quantity: Int
    ) {
        val productsMap = state.value.products.toMutableMap()
        val productList = productsMap[product.category]?.toMutableList() ?: return
        val productIndex = productList.indexOf(product)

        productList[productIndex] = product.copy(quantity = quantity)
        productsMap[product.category] = productList.toList()

        _state.update {
            it.copy(
                products = productsMap.toMap()
            )
        }
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