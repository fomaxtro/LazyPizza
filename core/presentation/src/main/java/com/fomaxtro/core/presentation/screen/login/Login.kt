package com.fomaxtro.core.presentation.screen.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fomaxtro.core.presentation.R
import com.fomaxtro.core.presentation.designsystem.button.LazyPizzaButton
import com.fomaxtro.core.presentation.designsystem.text_field.LazyPizzaOutlinedFormTextField
import com.fomaxtro.core.presentation.designsystem.theme.LazyPizzaTheme
import com.fomaxtro.core.presentation.designsystem.theme.body3Regular
import com.fomaxtro.core.presentation.designsystem.theme.textSecondary
import com.fomaxtro.core.presentation.designsystem.theme.title1Medium
import com.fomaxtro.core.presentation.designsystem.theme.title3
import com.fomaxtro.core.presentation.screen.login.component.OutlinedOtpField
import com.fomaxtro.core.presentation.ui.ObserveAsEvents
import com.fomaxtro.core.presentation.ui.ScreenType
import com.fomaxtro.core.presentation.ui.UiText
import com.fomaxtro.core.presentation.ui.currentScreenType
import kotlin.time.Duration.Companion.seconds

@Composable
fun LoginRoot(
    onNavigateToHome: () -> Unit,
    viewModel: LoginViewModel
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is LoginEvent.ShowMessage -> {
                snackbarHostState.showSnackbar(
                    message = event.message.asString(context)
                )
            }

            LoginEvent.NavigateToHome -> onNavigateToHome()
        }
    }

    LoginScreen(
        onAction = { action ->
            when (action) {
                LoginAction.OnContiueWithoutLoginClick -> onNavigateToHome()
                else -> viewModel.onAction(action)
            }
        },
        state = state,
        snackbarHostState = snackbarHostState
    )
}

@Composable
private fun LoginScreen(
    state: LoginState,
    onAction: (LoginAction) -> Unit = {},
    snackbarHostState: SnackbarHostState
) {
    val focusManager = LocalFocusManager.current
    val screenType = currentScreenType()

    Scaffold(
        modifier = Modifier
            .pointerInput(Unit) {
                detectTapGestures {
                    focusManager.clearFocus()
                }
            },
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .then(
                    if (screenType == ScreenType.WIDE_SCREEN) {
                        Modifier.width(400.dp)
                    } else Modifier
                )
                .verticalScroll(rememberScrollState())
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.welcome_to_lazypizza),
                style = MaterialTheme.typography.title1Medium
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = stringResource(R.string.enter_code),
                style = MaterialTheme.typography.body3Regular,
                color = MaterialTheme.colorScheme.textSecondary
            )

            Spacer(modifier = Modifier.height(20.dp))

            LazyPizzaOutlinedFormTextField(
                value = state.phoneNumber,
                onValueChange = {
                    onAction(LoginAction.OnPhoneNumberChange(it))
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = stringResource(R.string.phone_number_placeholder),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
            )

            AnimatedVisibility(state.isOtpInputVisible) {
                Column {
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedOtpField(
                        state = state.otpCode,
                        fields = 6,
                        placeholder = "000000",
                        modifier = Modifier.fillMaxWidth(),
                        error = state.otpError?.asString()
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyPizzaButton(
                onClick = {
                    focusManager.clearFocus()
                    onAction(LoginAction.SubmitLogin)
                },
                text = stringResource(R.string.continue_text),
                modifier = Modifier.fillMaxWidth(),
                enabled = state.canSubmitLogin,
                loading = state.isSubmittingLogin
            )

            TextButton(
                onClick = {
                    onAction(LoginAction.OnContiueWithoutLoginClick)
                }
            ) {
                Text(
                    text = stringResource(R.string.continue_without_signing),
                    style = MaterialTheme.typography.title3
                )
            }

            if (state.canResendOtp) {
                TextButton(
                    onClick = {
                        onAction(LoginAction.OnResendCodeClick)
                    }
                ) {
                    Text(
                        text = stringResource(R.string.resend_code),
                        style = MaterialTheme.typography.title3
                    )
                }
            } else if (state.isOtpInputVisible) {
                val formatRemainingTime =
                    state.remainingOtpResendTime.toComponents { minutes, seconds, _ ->
                        "%02d:%02d".format(minutes, seconds)
                    }

                Text(
                    text = stringResource(R.string.resend_code_countdown, formatRemainingTime),
                    style = MaterialTheme.typography.body3Regular,
                    color = MaterialTheme.colorScheme.textSecondary
                )
            }
        }
    }
}

@Preview(device = "id:pixel_tablet")
@Composable
private fun LoginScreenPreview() {
    LazyPizzaTheme {
        LoginScreen(
            state = LoginState(
                isOtpInputVisible = true,
                canResendOtp = false,
                otpError = UiText.DynamicString("Incorrect code. Please try again"),
                remainingOtpResendTime = 30.seconds
            ),
            snackbarHostState = SnackbarHostState()
        )
    }
}