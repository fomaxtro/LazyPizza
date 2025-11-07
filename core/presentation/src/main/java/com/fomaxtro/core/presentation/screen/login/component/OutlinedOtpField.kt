package com.fomaxtro.core.presentation.screen.login.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.delete
import androidx.compose.foundation.text.input.placeCursorAtEnd
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.fomaxtro.core.presentation.designsystem.theme.LazyPizzaTheme
import com.fomaxtro.core.presentation.designsystem.theme.body2Regular
import com.fomaxtro.core.presentation.designsystem.theme.surfaceHighest
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.drop

@Composable
fun OutlinedOtpField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    fields: Int = 4
) {
    val otpStates = remember {
        List(fields) { OtpState() }
    }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(fields) {
        val optStatesFlow = otpStates.map {
            snapshotFlow { it.state.text.toString() }
        }

        combine(optStatesFlow) { states ->
            val result = states.joinToString("")

            state.setTextAndPlaceCursorAtEnd(result)
        }.collect()
    }

    Row(
        modifier = modifier
    ) {
        otpStates.forEachIndexed { index, otpState ->
            LaunchedEffect(otpState.state) {
                snapshotFlow { otpState.state.text.toString() }
                    .drop(1)
                    .collect { text ->
                        when {
                            index + 1 == fields -> focusManager.clearFocus()

                            text.isNotEmpty() -> {
                                otpStates.getOrNull(index + 1)?.focusRequester?.requestFocus()
                            }

                            else -> {
                                otpStates.getOrNull(index - 1)?.focusRequester?.requestFocus()
                            }
                        }
                    }
            }

            LaunchedEffect(otpState.state) {
                snapshotFlow { otpState.state.selection }
                    .drop(1)
                    .collect { selection ->
                        if (selection.end != otpState.state.text.length) {
                            otpState.state.edit {
                                placeCursorAtEnd()
                            }
                        }
                    }
            }

            BasicTextField(
                state = otpState.state,
                decorator = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .height(OutlinedTextFieldDefaults.MinHeight)
                            .background(
                                color = MaterialTheme.colorScheme.surfaceHighest,
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        innerTextField()
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(otpState.focusRequester)
                    .focusProperties {
                        val hasPreviousCode = otpStates.getOrNull(index - 1)
                            ?.state
                            ?.text
                            ?.isNotEmpty() ?: false

                        canFocus = index == 0 || hasPreviousCode
                    },
                textStyle = MaterialTheme.typography.body2Regular.copy(
                    textAlign = TextAlign.Center
                ),
                inputTransformation = {
                    if (length > 1) {
                        delete(0, 1)
                    }
                }
            )
        }
    }
}

private data class OtpState(
    val state: TextFieldState = TextFieldState(),
    val focusRequester: FocusRequester = FocusRequester()
)

@Preview(showBackground = false)
@Composable
private fun OutlinedOtpFieldPreview() {
    val state = rememberTextFieldState()

    LazyPizzaTheme {
        Column {
            Text(state.text.toString())
            OutlinedOtpField(
                state = state,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}