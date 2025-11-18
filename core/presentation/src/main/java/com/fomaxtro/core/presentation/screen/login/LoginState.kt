package com.fomaxtro.core.presentation.screen.login

import androidx.compose.foundation.text.input.TextFieldState
import com.fomaxtro.core.presentation.ui.UiText
import kotlin.time.Duration

data class LoginState(
    val phoneNumber: String = "",
    val otpCode: TextFieldState = TextFieldState(),
    val isOtpInputVisible: Boolean = false,
    val otpError: UiText? = null,
    val canResendOtp: Boolean = false,
    val canSubmitLogin: Boolean = false,
    val isSubmittingLogin: Boolean = false,
    val remainingOtpResendTime: Duration = Duration.ZERO
)