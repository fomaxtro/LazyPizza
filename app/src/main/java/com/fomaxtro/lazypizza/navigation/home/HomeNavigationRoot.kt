package com.fomaxtro.lazypizza.navigation.home

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.togetherWith
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.fomaxtro.core.presentation.screen.cart.CartRoot
import com.fomaxtro.core.presentation.screen.history.HistoryRoot
import com.fomaxtro.core.presentation.screen.home.HomeDestination
import com.fomaxtro.core.presentation.screen.home.HomeRoot
import com.fomaxtro.core.presentation.screen.menu.MenuRoot
import kotlinx.coroutines.launch

@Composable
fun HomeNavigationRoot(
    onNavigateToProductDetails: (productId: Long) -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToCheckout: () -> Unit
) {
    val backStack = rememberNavBackStack(HomeRoute.Menu)
    val currentRoute = backStack.lastOrNull()

    val currentDestination = when (currentRoute) {
        HomeRoute.Menu -> HomeDestination.MENU
        HomeRoute.Cart -> HomeDestination.CART
        HomeRoute.History -> HomeDestination.HISTORY
        else -> HomeDestination.MENU
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    HomeRoot(
        currentDestination = currentDestination,
        onDestinationClick = { destination ->
            val route = when (destination) {
                HomeDestination.MENU -> HomeRoute.Menu
                HomeDestination.CART -> HomeRoute.Cart
                HomeDestination.HISTORY -> HomeRoute.History
            }

            backStack.add(route)
            backStack.remove(currentRoute)
        },
        onNavigateToLogin = onNavigateToLogin,
        hostState = snackbarHostState
    ) {
        NavDisplay(
            backStack = backStack,
            onBack = {
                backStack.removeLastOrNull()
            },
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator()
            ),
            transitionSpec = {
                EnterTransition.None togetherWith ExitTransition.None
            },
            popTransitionSpec = {
                EnterTransition.None togetherWith ExitTransition.None
            },
            predictivePopTransitionSpec = {
                EnterTransition.None togetherWith ExitTransition.None
            },
            entryProvider = entryProvider {
                entry<HomeRoute.Menu> {
                    MenuRoot(
                        onProductClick = onNavigateToProductDetails,
                        onShowMessage = { message ->
                            scope.launch {
                                snackbarHostState.showSnackbar(message.asString(context))
                            }
                        }
                    )
                }

                entry<HomeRoute.Cart> {
                    CartRoot(
                        onBackToMenuClick = {
                            val currentRoute = backStack.lastOrNull()

                            if (currentRoute is HomeRoute.Cart) {
                                backStack.add(HomeRoute.Menu)
                                backStack.remove(currentRoute)
                            }
                        },
                        onNavigateToCheckout = onNavigateToCheckout
                    )
                }

                entry<HomeRoute.History> {
                    HistoryRoot(
                        onNavigateToLogin = onNavigateToLogin
                    )
                }
            }
        )
    }
}