package com.fomaxtro.core.presentation.screen.home

sealed interface HomeAction {
    data object OnLogoutClick : HomeAction
    data object OnLogoutConfirmClick : HomeAction
    data object OnLogoutDismiss : HomeAction
    data object OnLoginClick : HomeAction
}