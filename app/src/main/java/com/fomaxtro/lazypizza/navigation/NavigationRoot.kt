package com.fomaxtro.lazypizza.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.fomaxtro.core.presentation.screen.product_details.ProductDetailsRoot
import com.fomaxtro.lazypizza.navigation.home.HomeNavigationRoot

@Composable
fun NavigationRoot() {
    val backStack = rememberNavBackStack(Route.Home)

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
                    }
                )
            }
        }
    )
}