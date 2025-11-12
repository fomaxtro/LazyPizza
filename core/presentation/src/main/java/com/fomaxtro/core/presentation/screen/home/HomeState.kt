package com.fomaxtro.core.presentation.screen.home

data class HomeState(
    val cartItemsCount: Int = 0,
    val isAuthenticated: Boolean = false,
    val isLogoutDialogVisible: Boolean = false
)
