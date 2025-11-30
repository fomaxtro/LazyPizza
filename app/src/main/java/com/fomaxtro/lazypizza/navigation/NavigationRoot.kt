package com.fomaxtro.lazypizza.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.fomaxtro.core.presentation.screen.checkout.CheckoutRoot
import com.fomaxtro.core.presentation.screen.login.LoginRoot
import com.fomaxtro.core.presentation.screen.order_confirmation.OrderConfirmation
import com.fomaxtro.core.presentation.screen.product_details.ProductDetailsRoot
import com.fomaxtro.lazypizza.navigation.home.HomeNavigationRoot
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun NavigationRoot() {
    val backStack = rememberNavBackStack(Route.Home())

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
                    },
                    onNavigateToCheckout = {
                        if (backStack.lastOrNull() is Route.Home) {
                            backStack.add(Route.Checkout)
                        }
                    }
                )
            }

            entry<Route.ProductDetails> { entry ->
                ProductDetailsRoot(
                    viewModel = koinViewModel {
                        parametersOf(entry.productId)
                    },
                    onBackClick = {
                        if (backStack.lastOrNull() is Route.ProductDetails) {
                            backStack.removeLastOrNull()
                        }
                    },
                    onNavigateToCart = {
                        val currentRoute = backStack.lastOrNull()
                        val lastHome = backStack.lastOrNull { it is Route.Home }

                        if (currentRoute is Route.ProductDetails) {
                            backStack.add(Route.Home())
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
                        backStack.add(Route.Home())
                        backStack.remove(previousRoute)
                    },
                    viewModel = koinViewModel()
                )
            }

            entry<Route.Checkout> {
                CheckoutRoot(
                    onNavigateBack = {
                        if (backStack.lastOrNull() is Route.Checkout) {
                            backStack.removeLastOrNull()
                        }
                    },
                    viewModel = koinViewModel(),
                    onNavigateToOrderConfirmation = { orderId, pickupTimeUtc ->
                        if (backStack.lastOrNull() is Route.Checkout) {
                            backStack.add(
                                Route.OrderConfirmation(
                                    orderId = orderId,
                                    pickupTimeUtc = pickupTimeUtc
                                )
                            )
                        }
                    }
                )
            }

            entry<Route.OrderConfirmation> { route ->
                OrderConfirmation(
                    onNavigateBack = {
                        if (backStack.lastOrNull() is Route.OrderConfirmation) {
                            backStack.removeLastOrNull()
                        }
                    },
                    onNavigateToMenu = {
                        val homeRouteIndex = backStack.indexOfLast { it is Route.Home }

                        if (homeRouteIndex != -1) {
                            val routesToRemove =
                                backStack.slice(homeRouteIndex..backStack.lastIndex)

                            backStack.removeAll(routesToRemove)
                            backStack.add(Route.Home())
                        } else {
                            throw IllegalStateException("Home route not found in back stack")
                        }
                    },
                    orderId = route.orderId,
                    pickupTimeUtc = route.pickupTimeUtc
                )
            }
        }
    )
}