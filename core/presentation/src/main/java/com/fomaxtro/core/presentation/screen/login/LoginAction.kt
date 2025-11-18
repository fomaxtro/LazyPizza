package com.fomaxtro.core.presentation.screen.login

sealed interface LoginAction {
    data class OnPhoneNumberChange(val phoneNumber: String) : LoginAction
    data object OnResendCodeClick : LoginAction
    data object SubmitLogin : LoginAction
    data object OnContiueWithoutLoginClick : LoginAction
}