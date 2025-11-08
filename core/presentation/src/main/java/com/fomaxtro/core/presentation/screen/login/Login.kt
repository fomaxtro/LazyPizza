package com.fomaxtro.core.presentation.screen.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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
import com.fomaxtro.core.presentation.ui.UiText

@Composable
fun LoginRoot(
    viewModel: LoginViewModel
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LoginScreen(
        onAction = viewModel::onAction,
        state = state
    )
}

@Composable
private fun LoginScreen(
    state: LoginState,
    onAction: (LoginAction) -> Unit = {}
) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
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
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                placeholder = stringResource(R.string.phone_number_placeholder)
            )

            if (state.isOtpInputVisible) {
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedOtpField(
                    state = state.otpCode,
                    fields = 6,
                    placeholder = "000000",
                    modifier = Modifier.fillMaxWidth(),
                    error = state.otpError?.asString()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyPizzaButton(
                onClick = {},
                text = stringResource(R.string.continue_text),
                modifier = Modifier.fillMaxWidth(),
                enabled = state.canSubmitLogin
            )

            TextButton(
                onClick = {}
            ) {
                Text(
                    text = stringResource(R.string.continue_without_signing),
                    style = MaterialTheme.typography.title3
                )
            }

            if (state.canResendOtp) {
                TextButton(
                    onClick = {}
                ) {
                    Text(
                        text = stringResource(R.string.resend_code),
                        style = MaterialTheme.typography.title3
                    )
                }
            } else if (state.isOtpInputVisible) {
                Text(
                    text = stringResource(R.string.resend_code_countdown, "00:10"),
                    style = MaterialTheme.typography.body3Regular,
                    color = MaterialTheme.colorScheme.textSecondary
                )
            }
        }
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    LazyPizzaTheme {
        LoginScreen(
            state = LoginState(
                isOtpInputVisible = true,
                canResendOtp = false,
                otpError = UiText.DynamicString("Incorrect code. Please try again")
            )
        )
    }
}