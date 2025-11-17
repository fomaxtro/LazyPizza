package com.fomaxtro.core.presentation.screen.history

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fomaxtro.core.presentation.R
import com.fomaxtro.core.presentation.component.EmptyInfo
import com.fomaxtro.core.presentation.designsystem.button.LazyPizzaButton
import com.fomaxtro.core.presentation.designsystem.theme.LazyPizzaTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun HistoryRoot(
    viewModel: HistoryViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    HistoryScreen(
        onAction = viewModel::onAction,
        state = state
    )
}

@Composable
private fun HistoryScreen(
    onAction: (HistoryAction) -> Unit = {},
    state: HistoryState
) {
    if (!state.isAuthenticated) {
        EmptyInfo(
            title = stringResource(R.string.not_signed_in),
            subtitle = stringResource(R.string.not_signed_in_subtitle),
            action = {
                LazyPizzaButton(
                    onClick = {},
                    text = stringResource(R.string.sign_in)
                )
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HistoryScreenPreview() {
    LazyPizzaTheme {
        HistoryScreen(
            state = HistoryState()
        )
    }
}