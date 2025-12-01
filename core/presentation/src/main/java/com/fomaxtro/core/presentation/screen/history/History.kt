package com.fomaxtro.core.presentation.screen.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fomaxtro.core.presentation.R
import com.fomaxtro.core.presentation.component.EmptyInfo
import com.fomaxtro.core.presentation.designsystem.button.LazyPizzaButton
import com.fomaxtro.core.presentation.designsystem.theme.LazyPizzaTheme
import com.fomaxtro.core.presentation.screen.history.component.HistoryContentLoader
import com.fomaxtro.core.presentation.screen.history.component.HistoryListItem
import com.fomaxtro.core.presentation.ui.Resource
import com.fomaxtro.core.presentation.ui.ScreenType
import com.fomaxtro.core.presentation.ui.currentScreenType

@Composable
fun HistoryRoot(
    onNavigateToLogin: () -> Unit,
    viewModel: HistoryViewModel
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    HistoryScreen(
        state = state,
        onAction = { action ->
            when (action) {
                HistoryAction.OnSignInClick -> onNavigateToLogin()
            }
        }
    )
}

@Composable
private fun HistoryScreen(
    onAction: (HistoryAction) -> Unit = {},
    state: HistoryState
) {
    val screenType = currentScreenType()

    when (state.isAuthenticated) {
        is Resource.Success -> {
            if (state.isAuthenticated.data) {
                when (state.orders) {
                    is Resource.Success -> {
                        when (screenType) {
                            ScreenType.MOBILE -> {
                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 16.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    items(state.orders.data) { order ->
                                        HistoryListItem(
                                            order = order
                                        )
                                    }
                                }
                            }
                            ScreenType.WIDE_SCREEN -> {
                                LazyVerticalStaggeredGrid(
                                    columns = StaggeredGridCells.Fixed(2),
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 16.dp),
                                    verticalItemSpacing = 8.dp,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    items(state.orders.data) { order ->
                                        HistoryListItem(
                                            order = order
                                        )
                                    }
                                }
                            }
                        }
                    }

                    else -> HistoryContentLoader()
                }
            } else {
                EmptyInfo(
                    title = stringResource(R.string.not_signed_in),
                    subtitle = stringResource(R.string.not_signed_in_subtitle),
                    action = {
                        LazyPizzaButton(
                            onClick = {
                                onAction(HistoryAction.OnSignInClick)
                            },
                            text = stringResource(R.string.sign_in)
                        )
                    }
                )
            }
        }

        else -> HistoryContentLoader()
    }
}

@Preview(showBackground = true)
@Composable
private fun HistoryScreenPreview() {
    LazyPizzaTheme {
        HistoryScreen(
            state = HistoryState(
                isAuthenticated = Resource.Loading
            )
        )
    }
}