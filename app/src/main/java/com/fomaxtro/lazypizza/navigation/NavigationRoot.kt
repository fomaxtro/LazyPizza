package com.fomaxtro.lazypizza.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.fomaxtro.core.presentation.screen.login.LoginRoot
import com.fomaxtro.core.presentation.screen.product_details.ProductDetailsRoot
import com.fomaxtro.lazypizza.navigation.home.HomeNavigationRoot

@Composable
fun NavigationRoot(
    isAuthenticated: Boolean
) {
    val backStack = rememberNavBackStack(if (isAuthenticated) Route.Home else Route.Login)

    NavDisplay(
        backStack = backStack,
        onBack = {
            backStack.removeLastOrNull()
        },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<Route.Home> {
                HomeNavigationRoot(
                    onNavigateToProductDetails = { productId ->
                        if (backStack.lastOrNull() !is Route.ProductDetails) {
                            backStack.add(Route.ProductDetails(productId))
                        }
                    },
                    onNavigateToLogin = {
                        val currentRoute = backStack.lastOrNull()

                        if (currentRoute is Route.Home) {
                            backStack.add(Route.Login)
                            backStack.remove(currentRoute)
                        }
                    }
                )
            }

            entry<Route.ProductDetails> { entry ->
                ProductDetailsRoot(
                    productId = entry.productId,
                    onBackClick = {
                        if (backStack.lastOrNull() is Route.ProductDetails) {
                            backStack.removeLastOrNull()
                        }
                    },
                    onNavigateToCart = {
                        val currentRoute = backStack.lastOrNull()
                        val lastHome = backStack.lastOrNull { it is Route.Home }

                        if (currentRoute is Route.ProductDetails) {
                            backStack.add(Route.Home)
                            backStack.remove(currentRoute)
                            backStack.remove(lastHome)
                        }
                    }
                )
            }

            entry<Route.Login> {
                LoginRoot(
                    onNavigateToHome = {
                        val previousRoute = backStack.last()
                        backStack.add(Route.Home)
                        backStack.remove(previousRoute)
                    }
                )
            }
        }
    )
}