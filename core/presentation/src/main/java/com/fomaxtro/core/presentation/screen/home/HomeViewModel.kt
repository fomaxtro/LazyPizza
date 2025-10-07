package com.fomaxtro.core.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fomaxtro.core.domain.model.ProductCategory
import com.fomaxtro.core.domain.repository.ProductRepository
import com.fomaxtro.core.domain.util.Result
import com.fomaxtro.core.presentation.mapper.toUiText
import com.fomaxtro.core.presentation.screen.home.mapper.toProductUi
import com.fomaxtro.core.presentation.screen.home.model.ProductUi
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

class HomeViewModel(
    private val productRepository: ProductRepository
) : ViewModel() {
    private var firstLoad = false
    private var products = emptyList<ProductUi>()

    private val _state = MutableStateFlow(HomeState())
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
            HomeState()
        )

    private val eventChannel = Channel<HomeEvent>()
    val events = eventChannel.receiveAsFlow()

    private suspend fun loadProducts() {
        _state.update { it.copy(isLoading = true) }

        val result = productRepository.getAllProducts()

        _state.update { it.copy(isLoading = false) }


        when (result) {
            is Result.Error -> {
                eventChannel.send(
                    HomeEvent.ShowSystemMessage(result.error.toUiText())
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
    }

    private fun observeFilters() {
        val search = state
            .map { it.search }
            .distinctUntilChanged()

        val selectedCategories = state
            .map { it.selectedCategories }
            .distinctUntilChanged()

        combine(search, selectedCategories) { search, selectedCategories ->
            products
                .filter { product ->
                    if (selectedCategories.isEmpty()) {
                        true
                    } else {
                        selectedCategories.contains(product.category)
                    }
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

    fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.OnSearchChange -> onSearchChange(action.search)

            is HomeAction.OnProductCategoryToggle -> {
                onProductCategoryToggle(action.category)
            }
        }
    }

    private fun onProductCategoryToggle(category: ProductCategory) {
        val selectedCategories = state.value.selectedCategories.toMutableSet()

        if (selectedCategories.contains(category)) {
            selectedCategories.remove(category)
        } else {
            selectedCategories.add(category)
        }

        _state.update {
            it.copy(
                selectedCategories = selectedCategories.toSet()
            )
        }
    }

    private fun onSearchChange(search: String) {
        _state.update { it.copy(search = search) }
    }
}