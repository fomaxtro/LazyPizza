package com.fomaxtro.core.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fomaxtro.core.domain.repository.AuthRepository
import com.fomaxtro.core.domain.repository.CartRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val authRepository: AuthRepository,
    cartRepository: CartRepository
) : ViewModel() {
    private val _state = MutableStateFlow(HomeInternalState())

    val state = combine(
        _state,
        cartRepository.countCartItems(),
        authRepository.isAuthenticated()
    ) { state, cartItemsCount, isAuthenticated ->
        HomeState(
            cartItemsCount = cartItemsCount,
            isAuthenticated = isAuthenticated,
            isLogoutDialogVisible = state.isLogoutDialogVisible
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        HomeState()
    )

    fun onAction(action: HomeAction) {
        when (action) {
            HomeAction.OnLogoutClick -> onLogoutClick()
            HomeAction.OnLogoutConfirmClick -> onLogoutConfirmClick()
            HomeAction.OnLogoutDismiss -> onLogoutDismiss()
            else -> Unit
        }
    }

    private fun onLogoutDismiss() {
        _state.update { it.copy(isLogoutDialogVisible = false) }
    }

    private fun onLogoutConfirmClick() = viewModelScope.launch {
        _state.update { it.copy(isLogoutDialogVisible = false) }

        authRepository.logout()
    }

    private fun onLogoutClick() {
        _state.update { it.copy(isLogoutDialogVisible = true) }
    }

    private data class HomeInternalState(
        val isLogoutDialogVisible: Boolean = false
    )
}