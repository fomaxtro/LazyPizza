package com.fomaxtro.core.presentation.screen.cart

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fomaxtro.core.presentation.R
import com.fomaxtro.core.presentation.component.ProductListItem
import com.fomaxtro.core.presentation.designsystem.button.LazyPizzaButton
import com.fomaxtro.core.presentation.designsystem.theme.LazyPizzaTheme
import com.fomaxtro.core.presentation.designsystem.theme.body3Regular
import com.fomaxtro.core.presentation.designsystem.theme.textSecondary
import com.fomaxtro.core.presentation.designsystem.theme.title1Medium
import com.fomaxtro.core.presentation.model.CartItemUi
import com.fomaxtro.core.presentation.screen.cart.component.CartLayout
import com.fomaxtro.core.presentation.screen.cart.component.ProductRecommendationCard
import com.fomaxtro.core.presentation.ui.Formatters
import com.fomaxtro.core.presentation.ui.ObserveAsEvents
import com.fomaxtro.core.presentation.ui.ScreenType
import com.fomaxtro.core.presentation.ui.currentScreenType
import com.fomaxtro.core.presentation.util.ProductUiFactory
import com.fomaxtro.core.presentation.util.toDisplayText
import org.koin.androidx.compose.koinViewModel

@Composable
fun CartRoot(
    onBackToMenuClick: () -> Unit,
    viewModel: CartViewModel = koinViewModel()
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
    val screenType = currentScreenType()
    val horizontalPadding = when (screenType) {
        ScreenType.MOBILE -> 16.dp
        ScreenType.WIDE_SCREEN -> 181.dp
    }

    if (!state.isCartItemsLoading && state.cartItems.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 120.dp)
                .padding(horizontal = horizontalPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.empty_cart),
                style = MaterialTheme.typography.title1Medium,
                textAlign = TextAlign.Center
            )

            Text(
                text = stringResource(R.string.empty_cart_subtitle),
                style = MaterialTheme.typography.body3Regular,
                color = MaterialTheme.colorScheme.textSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            LazyPizzaButton(
                onClick = {
                    onAction(CartAction.OnBackToMenuClick)
                },
                text = stringResource(R.string.back_to_menu)
            )
        }
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
                    price = cartItem.totalPrice,
                    quantity = cartItem.quantity,
                    modifier = Modifier.fillMaxWidth(),
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
                    }
                )
            },
            loading = state.isCartItemsLoading,
            action = {
                LazyPizzaButton(
                    onClick = {},
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