package com.fomaxtro.core.presentation.screen.cart

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fomaxtro.core.presentation.R
import com.fomaxtro.core.presentation.component.EmptyInfo
import com.fomaxtro.core.presentation.component.ProductListItem
import com.fomaxtro.core.presentation.designsystem.button.LazyPizzaButton
import com.fomaxtro.core.presentation.designsystem.theme.LazyPizzaTheme
import com.fomaxtro.core.presentation.model.CartItemUi
import com.fomaxtro.core.presentation.screen.cart.component.CartLayout
import com.fomaxtro.core.presentation.component.ProductRecommendationCard
import com.fomaxtro.core.presentation.ui.Formatters
import com.fomaxtro.core.presentation.ui.ObserveAsEvents
import com.fomaxtro.core.presentation.util.ProductUiFactory
import com.fomaxtro.core.presentation.util.toDisplayText

@Composable
fun CartRoot(
    onBackToMenuClick: () -> Unit,
    onNavigateToCheckout: () -> Unit,
    viewModel: CartViewModel
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is CartEvent.ShowSystemMessage -> {
                Toast.makeText(
                    context,
                    event.message.asString(context),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    CartScreen(
        onAction = { action ->
            when (action) {
                CartAction.OnBackToMenuClick -> onBackToMenuClick()
                CartAction.OnCheckoutClick -> onNavigateToCheckout()
                else -> viewModel.onAction(action)
            }
        },
        state = state
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CartScreen(
    onAction: (CartAction) -> Unit = {},
    state: CartState
) {
    if (!state.isCartItemsLoading && state.cartItems.isEmpty()) {
        EmptyInfo(
            title = stringResource(R.string.empty_cart),
            subtitle = stringResource(R.string.empty_cart_subtitle),
            action = {
                LazyPizzaButton(
                    onClick = {
                        onAction(CartAction.OnBackToMenuClick)
                    },
                    text = stringResource(R.string.back_to_menu)
                )
            }
        )
    } else {
        CartLayout(
            cartItems = state.cartItems,
            productItemContent = { cartItem ->
                val product = cartItem.product
                val description = if (cartItem.selectedToppings.isNotEmpty()) {
                    cartItem.selectedToppings.joinToString("\n") {
                        it.toDisplayText()
                    }
                } else {
                    product.description
                }

                ProductListItem(
                    imageUrl = product.imageUrl,
                    name = product.name,
                    description = description,
                    price = cartItem.priceWithToppings,
                    quantity = cartItem.quantity,
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateItem(),
                    onQuantityChange = {
                        onAction(
                            CartAction.OnQuantityChange(
                                cartItemId = cartItem.id,
                                quantity = it
                            )
                        )
                    },
                    onDeleteClick = {
                        onAction(
                            CartAction.OnQuantityChange(
                                cartItemId = cartItem.id,
                                quantity = 0
                            )
                        )
                    },
                    minQuantity = 1
                )
            },
            recommendations = state.productRecommendations,
            recommendationItemContent = { product ->
                ProductRecommendationCard(
                    product = product,
                    onAddClick = {
                        onAction(CartAction.OnRecommendationAddClick(productId = product.id))
                    },
                    modifier = Modifier.animateItem()
                )
            },
            loading = state.isCartItemsLoading,
            action = {
                LazyPizzaButton(
                    onClick = {
                        onAction(CartAction.OnCheckoutClick)
                    },
                    text = stringResource(
                        id = R.string.proceed_to_checkout,
                        Formatters.formatCurrency(state.totalPrice)
                    ),
                    enabled = state.cartItems.isNotEmpty(),
                    modifier = Modifier.fillMaxWidth()
                )
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview
@Composable
private fun CartScreenPreview() {
    val cartItems = (1..3).map {
        CartItemUi(
            id = it.toString(),
            product = ProductUiFactory.create(
                id = it.toLong()
            ),
            quantity = 1
        )
    }

    val productRecommendations = (1..3).map {
        ProductUiFactory.create(
            id = it.toLong()
        )
    }

    LazyPizzaTheme {
        CartScreen(
            state = CartState(
                isCartItemsLoading = false,
                cartItems = cartItems,
                productRecommendations = productRecommendations
            )
        )
    }
}