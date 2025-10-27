package com.fomaxtro.lazypizza.navigation.home

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.fomaxtro.core.presentation.screen.cart.CartRoot
import com.fomaxtro.core.presentation.screen.home.HomeDestination
import com.fomaxtro.core.presentation.screen.home.HomeRoot
import com.fomaxtro.core.presentation.screen.menu.MenuRoot

@Composable
fun HomeNavigationRoot(
    onNavigateToProductDetails: (productId: Long) -> Unit
) {
    val backStack = rememberNavBackStack(HomeRoute.Menu)
    val currentRoute = backStack.lastOrNull()

    val currentDestination = when (currentRoute) {
        HomeRoute.Menu -> HomeDestination.MENU
        HomeRoute.Cart -> HomeDestination.CART
        HomeRoute.History -> HomeDestination.HISTORY
        else -> HomeDestination.MENU
    }

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
        }
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
                        onProductClick = onNavigateToProductDetails
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
                        }
                    )
                }

                entry<HomeRoute.History> { }
            }
        )
    }
}