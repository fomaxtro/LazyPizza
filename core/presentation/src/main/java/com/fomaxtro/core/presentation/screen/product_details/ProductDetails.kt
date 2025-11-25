package com.fomaxtro.core.presentation.screen.product_details

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.fomaxtro.core.presentation.R
import com.fomaxtro.core.presentation.designsystem.button.LazyPizzaButton
import com.fomaxtro.core.presentation.designsystem.button.LazyPizzaNavigationIconButton
import com.fomaxtro.core.presentation.designsystem.modifier.shimmer
import com.fomaxtro.core.presentation.designsystem.theme.LazyPizzaTheme
import com.fomaxtro.core.presentation.designsystem.top_bar.LazyPizzaTopAppBar
import com.fomaxtro.core.presentation.model.ToppingSelectionUi
import com.fomaxtro.core.presentation.screen.product_details.component.ProductDetailsLayout
import com.fomaxtro.core.presentation.screen.product_details.component.ToppingListItem
import com.fomaxtro.core.presentation.ui.Formatters
import com.fomaxtro.core.presentation.ui.ObserveAsEvents
import com.fomaxtro.core.presentation.ui.ScreenType
import com.fomaxtro.core.presentation.ui.currentScreenType
import com.fomaxtro.core.presentation.util.ProductUiFactory
import com.fomaxtro.core.presentation.util.ToppingUiFactory

@Composable
fun ProductDetailsRoot(
    onBackClick: () -> Unit,
    onNavigateToCart: () -> Unit,
    viewModel: ProductDetailsViewModel
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is ProductDetailsEvent.ShowSystemMessage -> {
                Toast.makeText(
                    context,
                    event.message.asString(context),
                    Toast.LENGTH_LONG
                ).show()
            }

            ProductDetailsEvent.NavigateToCart -> onNavigateToCart()
        }
    }

    ProductDetailsScreen(
        onAction = { action ->
            when (action) {
                ProductDetailsAction.OnNavigateBackClick -> onBackClick()
                else -> viewModel.onAction(action)
            }
        },
        state = state
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProductDetailsScreen(
    onAction: (ProductDetailsAction) -> Unit = {},
    state: ProductDetailsState
) {
    val screenType = currentScreenType()

    Scaffold(
        topBar = {
            LazyPizzaTopAppBar(
                navigationIcon = {
                    LazyPizzaNavigationIconButton(
                        onClick = {
                            onAction(ProductDetailsAction.OnNavigateBackClick)
                        }
                    )
                }
            )
        },
        containerColor = when (screenType) {
            ScreenType.MOBILE -> MaterialTheme.colorScheme.surface
            ScreenType.WIDE_SCREEN -> MaterialTheme.colorScheme.background
        }
    ) { innerPadding ->
        ProductDetailsLayout(
            image = {
                if (state.product != null) {
                    AsyncImage(
                        model = state.product.imageUrl,
                        contentDescription = state.product.name,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .shimmer()
                    )
                }
            },
            title = {
                if (state.product != null) {
                    Text(state.product.name)
                } else {
                    Box(
                        modifier = Modifier
                            .height(24.dp)
                            .width(124.dp)
                            .shimmer()
                    )
                }
            },
            subtitle = {
                if (state.product != null) {
                    Text(state.product.description ?: "")
                } else {
                    Box(
                        modifier = Modifier
                            .height(16.dp)
                            .fillMaxWidth()
                            .shimmer()
                    )
                }
            },
            action = {
                LazyPizzaButton(
                    onClick = {
                        onAction(ProductDetailsAction.OnAddToCartClick)
                    },
                    text = stringResource(
                        R.string.add_to_cart_for,
                        Formatters.formatCurrency(state.totalPrice)
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = state.canAddToCart
                )
            },
            items = state.toppings,
            itemContent = { toppingSelection ->
                ToppingListItem(
                    toppingSelection = toppingSelection,
                    onClick = {
                        onAction(
                            ProductDetailsAction.OnToppingQuantityChange(
                                toppingId = toppingSelection.topping.id,
                                quantity = 1
                            )
                        )
                    },
                    onQuantityChange = {
                        onAction(
                            ProductDetailsAction.OnToppingQuantityChange(
                                toppingId = toppingSelection.topping.id,
                                quantity = it
                            )
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            loading = state.isToppingsLoading,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }
}

@Preview
@Composable
private fun ProductDetailsScreenPreview() {
    LazyPizzaTheme {
        ProductDetailsScreen(
            state = ProductDetailsState(
                product = ProductUiFactory.create(
                    id = 1,
                    name = "Margherita",
                    description = "Tomato sauce, Mozzarella, Fresh basil, Olive oil",
                    imageUrl = "https://nnsqlltlktnsiaqxwwwk.storage.supabase.co/storage/v1/s3/lazy-pizza-products/pizza/Meat%20Lovers.png?x-id=GetObject&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=9325183b5fa10e7256a20329645de8b9%2F20251008%2Fus-east-2%2Fs3%2Faws4_request&X-Amz-Date=20251008T025007Z&X-Amz-Expires=86400&X-Amz-SignedHeaders=host&X-Amz-Signature=a20d0d65d5dede1b505535fe3c0473260cbb00a507f849eb1f4c740e0c85ff70"
                ),
                isToppingsLoading = false,
                toppings = (1..12).map {
                    ToppingSelectionUi(
                        topping = ToppingUiFactory.create(
                            id = it.toLong()
                        )
                    )
                }
            ),
        )
    }
}