package com.fomaxtro.core.presentation.screen.cart

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.fomaxtro.core.presentation.designsystem.top_bar.LazyPizzaCenteredAlignedTopAppBar
import com.fomaxtro.core.presentation.screen.cart.component.CartLayout
import com.fomaxtro.core.presentation.screen.cart.component.ProductRecommendationCard
import com.fomaxtro.core.presentation.ui.Formatters
import com.fomaxtro.core.presentation.ui.ScreenType
import com.fomaxtro.core.presentation.ui.currentScreenType
import com.fomaxtro.core.presentation.util.ProductUiFactory

@Composable
fun CartRoot(
    viewModel: CartViewModel
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    CartScreen(
        onAction = viewModel::onAction,
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

    Scaffold(
        topBar = {
            LazyPizzaCenteredAlignedTopAppBar(
                title = stringResource(R.string.cart)
            )
        }
    ) { innerPadding ->
        if (!state.isProductsLoading && state.products.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding)
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
                    onClick = {},
                    text = stringResource(R.string.back_to_menu)
                )
            }
        } else {
            CartLayout(
                products = state.products,
                productItemContent = { product ->
                    ProductListItem(
                        imageUrl = product.imageUrl,
                        name = product.name,
                        description = product.description,
                        price = product.price,
                        quantity = product.quantity,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                recommendations = state.productRecommendations,
                recommendationItemContent = { product ->
                    ProductRecommendationCard(
                        product = product,
                        onAddClick = {}
                    )
                },
                loading = state.isProductsLoading,
                action = {
                    LazyPizzaButton(
                        onClick = {},
                        text = stringResource(
                            id = R.string.proceed_to_checkout,
                            Formatters.formatCurrency(state.totalPrice)
                        ),
                        enabled = state.products.isNotEmpty(),
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            )
        }
    }
}

@Preview
@Composable
private fun CartScreenPreview() {
    val products = (1..3).map {
        ProductUiFactory.create(
            id = it.toLong()
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
                isProductsLoading = false,
                products = products,
                productRecommendations = productRecommendations
            )
        )
    }
}