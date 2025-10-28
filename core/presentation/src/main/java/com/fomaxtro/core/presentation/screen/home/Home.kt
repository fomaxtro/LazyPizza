package com.fomaxtro.core.presentation.screen.home

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fomaxtro.core.presentation.R
import com.fomaxtro.core.presentation.component.AdaptiveScaffold
import com.fomaxtro.core.presentation.component.NavigationButton
import com.fomaxtro.core.presentation.component.NotificationBadge
import com.fomaxtro.core.presentation.designsystem.theme.AppIcons
import com.fomaxtro.core.presentation.designsystem.theme.LazyPizzaTheme
import com.fomaxtro.core.presentation.designsystem.top_bar.LazyPizzaCenteredAlignedTopAppBar
import com.fomaxtro.core.presentation.screen.home.component.MenuTopAppBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeRoot(
    viewModel: HomeViewModel = koinViewModel(),
    currentDestination: HomeDestination,
    onDestinationClick: (HomeDestination) -> Unit,
    hostState: SnackbarHostState,
    content: @Composable () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    HomeScreen(
        state = state,
        currentDestination = currentDestination,
        onDestinationClick = onDestinationClick,
        hostState = hostState,
        content = content
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    state: HomeState,
    currentDestination: HomeDestination,
    onDestinationClick: (HomeDestination) -> Unit,
    hostState: SnackbarHostState,
    content: @Composable () -> Unit
) {
    val focusManager = LocalFocusManager.current

    AdaptiveScaffold(
        topBar = {
            when (currentDestination) {
                HomeDestination.MENU -> MenuTopAppBar()

                HomeDestination.CART -> {
                    LazyPizzaCenteredAlignedTopAppBar(
                        title = stringResource(R.string.cart)
                    )
                }

                HomeDestination.HISTORY -> {
                    LazyPizzaCenteredAlignedTopAppBar(
                        title = stringResource(R.string.history)
                    )
                }
            }
        },
        navigation = {
            NavigationButton(
                selected = currentDestination == HomeDestination.MENU,
                onClick = {
                    if (currentDestination != HomeDestination.MENU) {
                        onDestinationClick(HomeDestination.MENU)
                    }
                },
                icon = {
                    Icon(
                        imageVector = AppIcons.Filled.Menu,
                        contentDescription = stringResource(R.string.menu)
                    )
                },
                label = stringResource(R.string.menu)
            )

            NavigationButton(
                selected = currentDestination == HomeDestination.CART,
                onClick = {
                    if (currentDestination != HomeDestination.CART) {
                        onDestinationClick(HomeDestination.CART)
                    }
                },
                icon = {
                    BadgedBox(
                        badge = {
                            if (state.cartItemsCount > 0) {
                                NotificationBadge(
                                    value = state.cartItemsCount
                                )
                            }
                        }
                    ) {
                        Icon(
                            imageVector = AppIcons.Filled.Cart,
                            contentDescription = stringResource(R.string.cart)
                        )
                    }
                },
                label = stringResource(R.string.cart)
            )

            NavigationButton(
                selected = currentDestination == HomeDestination.HISTORY,
                onClick = {
                    if (currentDestination != HomeDestination.HISTORY) {
                        onDestinationClick(HomeDestination.HISTORY)
                    }
                },
                icon = {
                    Icon(
                        imageVector = AppIcons.Filled.History,
                        contentDescription = stringResource(R.string.history)
                    )
                },
                label = stringResource(R.string.history)
            )
        },
        modifier = Modifier
            .pointerInput(Unit) {
                detectTapGestures {
                    focusManager.clearFocus()
                }
            },
        hostState = hostState
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            content()
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    LazyPizzaTheme {
        HomeScreen(
            state = HomeState(),
            onDestinationClick = {},
            currentDestination = HomeDestination.MENU,
            hostState = SnackbarHostState()
        ) {

        }
    }
}