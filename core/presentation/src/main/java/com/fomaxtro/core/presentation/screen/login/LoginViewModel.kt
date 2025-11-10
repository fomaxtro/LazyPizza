package com.fomaxtro.core.presentation.screen.login

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fomaxtro.core.domain.repository.AuthRepository
import com.fomaxtro.core.domain.util.Result
import com.fomaxtro.core.domain.validation.OtpValidator
import com.fomaxtro.core.domain.validation.PhoneNumberValidator
import com.fomaxtro.core.presentation.mapper.toUiText
import com.fomaxtro.core.presentation.util.countdownTimer
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class LoginViewModel(
    private val phoneNumberValidator: PhoneNumberValidator,
    private val otpValidator: OtpValidator,
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _state = MutableStateFlow(LoginInternalState())

    private val eventChannel = Channel<LoginEvent>()
    val events = eventChannel.receiveAsFlow()

    private val otpCodeFlow = snapshotFlow { _state.value.otpCode.text.toString() }
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            ""
        )

    private val isValidPhoneNumberFlow = _state
        .map { it.phoneNumber }
        .distinctUntilChanged()
        .map { phoneNumber ->
            val validationError = phoneNumberValidator.validate(phoneNumber)

            validationError == null
        }

    private val canSubmitLogin = combine(
        isValidPhoneNumberFlow,
        _state.map { it.isOtpInputVisible }.distinctUntilChanged(),
        otpCodeFlow
    ) { isValidPhoneNumber, isOtpInputVisible, otpCode ->
        if (isValidPhoneNumber && isOtpInputVisible) {
            otpValidator.validate(otpCode) == null
        } else {
            isValidPhoneNumber
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        false
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    private val remainingOtpResendTime = _state
        .filter { it.isOtpInputVisible }
        .map { it.canResendOtp }
        .distinctUntilChanged()
        .flatMapLatest { canResendOtp ->
            if (!canResendOtp) {
                countdownTimer(1.minutes)
            } else emptyFlow()
        }
        .onEach { remainingTime ->
            if (remainingTime == Duration.ZERO) {
                _state.update { it.copy(canResendOtp = true) }
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            1.minutes
        )

    val state = combine(
        _state,
        canSubmitLogin,
        remainingOtpResendTime
    ) { state, canSubmitLogin, remainingOtpResendTime ->
        state.toUi(
            canSubmitLogin = canSubmitLogin,
            remainingOtpResendTime = remainingOtpResendTime
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        LoginState()
    )

    fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.OnPhoneNumberChange -> onPhoneNumberChange(action.phoneNumber)
            LoginAction.SubmitLogin -> submitLogin()
            LoginAction.OnResendCodeClick -> onResendCodeClick()
        }
    }

    private fun onResendCodeClick() = viewModelScope.launch {
        _state.update { it.copy(canResendOtp = false) }

        val result = authRepository.sendOtp(_state.value.phoneNumber)

        if (result is Result.Error) {
            eventChannel.send(
                LoginEvent.ShowMessage(
                    message = result.error.toUiText()
                )
            )
        }
    }

    private fun onPhoneNumberChange(phoneNumber: String) {
        _state.update { it.copy(phoneNumber = phoneNumber) }
    }

    private fun submitLogin() = viewModelScope.launch {
        if (!_state.value.isOtpInputVisible) {
            _state.update { it.copy(isSubmittingLogin = true) }

            try {
                when (val result = authRepository.sendOtp(_state.value.phoneNumber)) {
                    is Result.Error -> {
                        eventChannel.send(
                            LoginEvent.ShowMessage(
                                message = result.error.toUiText()
                            )
                        )
                    }

                    is Result.Success -> {
                        _state.update { it.copy(isOtpInputVisible = true) }
                    }
                }
            } finally {
                _state.update { it.copy(isSubmittingLogin = false) }
            }

            return@launch
        }
    }

    private data class LoginInternalState(
        val phoneNumber: String = "",
        val isOtpInputVisible: Boolean = false,
        val otpCode: TextFieldState = TextFieldState(),
        val canResendOtp: Boolean = false,
        val isSubmittingLogin: Boolean = false
    )

    private fun LoginInternalState.toUi(
        canSubmitLogin: Boolean,
        remainingOtpResendTime: Duration
    ) = LoginState(
        phoneNumber = phoneNumber,
        otpCode = otpCode,
        isOtpInputVisible = isOtpInputVisible,
        otpError = null,
        canResendOtp = canResendOtp,
        canSubmitLogin = canSubmitLogin,
        isSubmittingLogin = isSubmittingLogin,
        remainingOtpResendTime = remainingOtpResendTime,
    )
}