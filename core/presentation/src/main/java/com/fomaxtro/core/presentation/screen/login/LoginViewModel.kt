package com.fomaxtro.core.presentation.screen.login

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fomaxtro.core.domain.repository.AuthRepository
import com.fomaxtro.core.domain.use_case.Login
import com.fomaxtro.core.domain.util.DataError
import com.fomaxtro.core.domain.util.Result
import com.fomaxtro.core.domain.util.ValidationResult
import com.fomaxtro.core.domain.validation.OtpValidator
import com.fomaxtro.core.domain.validation.PhoneNumberValidator
import com.fomaxtro.core.presentation.R
import com.fomaxtro.core.presentation.mapper.toUiText
import com.fomaxtro.core.presentation.ui.UiText
import com.fomaxtro.core.presentation.util.countdownTimer
import com.fomaxtro.core.presentation.verification.OtpCodeEventBus
import com.google.android.gms.auth.api.phone.SmsRetrieverClient
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class LoginViewModel(
    private val phoneNumberValidator: PhoneNumberValidator,
    private val otpValidator: OtpValidator,
    private val authRepository: AuthRepository,
    private val smsRetrieverClient: SmsRetrieverClient,
    private val otpCodeEventBus: OtpCodeEventBus,
    private val login: Login
) : ViewModel() {
    private val _state = MutableStateFlow(LoginInternalState())

    private val eventChannel = Channel<LoginEvent>()
    val events = eventChannel.receiveAsFlow()

    private val otpCodeFlow = snapshotFlow { _state.value.otpCode.text.toString() }

    private val isValidPhoneNumberFlow = _state
        .map { it.phoneNumber }
        .distinctUntilChanged()
        .map { phoneNumber ->
            phoneNumberValidator.validate(phoneNumber) is ValidationResult.Valid
        }

    private val canSubmitLogin = combine(
        isValidPhoneNumberFlow,
        _state.map { it.isOtpInputVisible }.distinctUntilChanged(),
        otpCodeFlow
    ) { isValidPhoneNumber, isOtpInputVisible, otpCode ->
        if (isValidPhoneNumber && isOtpInputVisible) {
            otpValidator.validate(otpCode) is ValidationResult.Valid
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

    private fun observeOtpCode() {
        otpCodeEventBus.events
            .onEach { otpCode ->
                _state.update {
                    it.copy(
                        otpCode = TextFieldState(otpCode)
                    )
                }

                submitLogin()
            }
            .launchIn(viewModelScope)
    }

    init {
        observeOtpCode()
    }

    fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.OnPhoneNumberChange -> onPhoneNumberChange(action.phoneNumber)
            LoginAction.SubmitLogin -> submitLogin()
            LoginAction.OnResendCodeClick -> onResendCodeClick()
            else -> Unit
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
        _state.update { it.copy(otpError = null) }

        if (!_state.value.isOtpInputVisible) {
            _state.update { it.copy(isSubmittingLogin = true) }

            try {
                smsRetrieverClient.startSmsRetriever().await()

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

        _state.update { it.copy(isSubmittingLogin = true) }

        try {
            val result = login(
                phoneNumber = _state.value.phoneNumber,
                code = _state.value.otpCode.text.toString()
            )

            when (result) {
                is Result.Error -> {
                    when (result.error) {
                        DataError.Validation.INVALID_INPUT -> {
                            _state.update {
                                it.copy(
                                    otpError = UiText.StringResource(R.string.incorrect_code)
                                )
                            }
                        }

                        else -> {
                            eventChannel.send(
                                LoginEvent.ShowMessage(
                                    message = result.error.toUiText()
                                )
                            )
                        }
                    }
                }

                is Result.Success -> {
                    eventChannel.send(LoginEvent.NavigateToHome)
                }
            }
        } finally {
            _state.update { it.copy(isSubmittingLogin = false) }
        }
    }

    private data class LoginInternalState(
        val phoneNumber: String = "",
        val isOtpInputVisible: Boolean = false,
        val otpCode: TextFieldState = TextFieldState(),
        val canResendOtp: Boolean = false,
        val isSubmittingLogin: Boolean = false,
        val otpError: UiText? = null
    )

    private fun LoginInternalState.toUi(
        canSubmitLogin: Boolean,
        remainingOtpResendTime: Duration
    ) = LoginState(
        phoneNumber = phoneNumber,
        otpCode = otpCode,
        isOtpInputVisible = isOtpInputVisible,
        otpError = otpError,
        canResendOtp = canResendOtp,
        canSubmitLogin = canSubmitLogin,
        isSubmittingLogin = isSubmittingLogin,
        remainingOtpResendTime = remainingOtpResendTime,
    )
}