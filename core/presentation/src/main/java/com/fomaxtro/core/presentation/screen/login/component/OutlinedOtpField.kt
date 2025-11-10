package com.fomaxtro.core.presentation.screen.login.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.fomaxtro.core.presentation.designsystem.theme.LazyPizzaTheme
import com.fomaxtro.core.presentation.designsystem.theme.body2Regular
import com.fomaxtro.core.presentation.designsystem.theme.body4Regular
import com.fomaxtro.core.presentation.designsystem.theme.surfaceHighest
import com.fomaxtro.core.presentation.designsystem.theme.textSecondary
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.drop

@OptIn(ExperimentalStdlibApi::class)
@Composable
fun OutlinedOtpField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    fields: Int = 4,
    placeholder: String? = null,
    shape: Shape = CircleShape,
    error: String? = null
) {
    val otpStates = remember(state, fields) {
        state.text
            .map { it.toString() }
            .toTypedArray()
            .copyOf(fields) { "" }
            .map {
                OtpState(
                    state = TextFieldState(it)
                )
            }
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

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.height(IntrinsicSize.Max)
    ) {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
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

                val isFocused by otpState.interactionSource.collectIsFocusedAsState()

                BasicTextField(
                    state = otpState.state,
                    decorator = { innerTextField ->
                        Box(
                            modifier = Modifier
                                .height(OutlinedTextFieldDefaults.MinHeight)
                                .background(
                                    color = MaterialTheme.colorScheme.surfaceHighest,
                                    shape = shape
                                )
                                .then(
                                    if (error != null) {
                                        Modifier
                                            .border(
                                                color = MaterialTheme.colorScheme.primary,
                                                width = 1.dp,
                                                shape = shape
                                            )
                                    } else Modifier
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (
                                otpState.state.text.isEmpty()
                                && placeholder != null
                                && !isFocused
                            ) {
                                Text(
                                    text = placeholder.getOrNull(index)?.toString() ?: "",
                                    style = MaterialTheme.typography.body2Regular,
                                    color = MaterialTheme.colorScheme.textSecondary
                                )
                            }

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
                        val currentText = asCharSequence().toString()

                        if (length == 1 && !currentText.isDigitsOnly()) {
                            delete(0, 1)
                        } else if (length > 1 && !currentText.isDigitsOnly()) {
                            delete(1, 2)
                        } else if (length > 1) {
                            delete(0, 1)
                        }
                    },
                    interactionSource = otpState.interactionSource,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    )
                )
            }
        }

        if (error != null) {
            Text(
                text = error,
                style = MaterialTheme.typography.body4Regular,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

private data class OtpState(
    val state: TextFieldState = TextFieldState(),
    val focusRequester: FocusRequester = FocusRequester(),
    val interactionSource: MutableInteractionSource = MutableInteractionSource()
)

@Preview(showBackground = false)
@Composable
private fun OutlinedOtpFieldPreview() {
    val state = rememberTextFieldState()

    LazyPizzaTheme {
        OutlinedOtpField(
            state = state,
            modifier = Modifier.fillMaxWidth(),
            placeholder = "000",
            error = "Wrong code"
        )
    }
}