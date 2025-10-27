package com.fomaxtro.core.presentation.screen.cart.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fomaxtro.core.presentation.R
import com.fomaxtro.core.presentation.component.GradientFadeBox
import com.fomaxtro.core.presentation.component.GradientFadeBoxDefaults
import com.fomaxtro.core.presentation.component.ProductListItem
import com.fomaxtro.core.presentation.component.ProductListItemLoader
import com.fomaxtro.core.presentation.designsystem.button.LazyPizzaButton
import com.fomaxtro.core.presentation.designsystem.theme.LazyPizzaTheme
import com.fomaxtro.core.presentation.designsystem.theme.label2Semibold
import com.fomaxtro.core.presentation.designsystem.theme.textSecondary
import com.fomaxtro.core.presentation.model.CartItemUi
import com.fomaxtro.core.presentation.model.ProductUi
import com.fomaxtro.core.presentation.model.ToppingSelectionUi
import com.fomaxtro.core.presentation.util.ProductUiFactory
import com.fomaxtro.core.presentation.util.ToppingUiFactory

@Composable
fun CartPhone(
    cartItems: List<CartItemUi>,
    productItemContent: @Composable LazyItemScope.(CartItemUi) -> Unit,
    recommendations: List<ProductUi>,
    recommendationItemContent: @Composable LazyItemScope.(ProductUi) -> Unit,
    loading: Boolean,
    action: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            if (loading) {
                items(3) {
                    ProductListItemLoader(
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                }
            } else {
                items(cartItems, key = { it.id }) { cartItem ->
                    productItemContent(cartItem)

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            item {
                Spacer(modifier = Modifier.height(12.dp))
            }

            item {
                Text(
                    text = stringResource(R.string.order_recommendation).uppercase(),
                    style = MaterialTheme.typography.label2Semibold,
                    color = MaterialTheme.colorScheme.textSecondary
                )
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (loading) {
                        items(3) {
                            ProductRecommendationCardLoader()
                        }
                    } else {
                        items(recommendations, key = { it.id }) { product ->
                            recommendationItemContent(product)
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(GradientFadeBoxDefaults.offsetSpacing))
            }
        }

        GradientFadeBox(
            modifier = Modifier.align(Alignment.BottomCenter),
            content = action
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CartPhonePreview() {
    val toppings = (1..2).map {
        ToppingSelectionUi(
            topping = ToppingUiFactory.create(
                id = it.toLong(),
                price = 2.0
            ),
            quantity = 2
        )
    }

    val cartItems = (1..2).map { id ->
        CartItemUi(
            id = "1",
            product = ProductUiFactory.create(
                id = id.toLong(),
                price = 10.99
            ),
            selectedToppings = toppings
        )
    }

    val recommendations = (1..5).map {
        ProductUiFactory.create(
            id = it.toLong()
        )
    }

    LazyPizzaTheme {
        CartPhone(
            action = {
                LazyPizzaButton(
                    onClick = {},
                    text = "Proceed to Checkout ($25.45)",
                    modifier = Modifier.fillMaxWidth()
                )
            },
            cartItems = cartItems,
            productItemContent = { cartItem ->
                val product = cartItem.product

                ProductListItem(
                    imageUrl = product.imageUrl,
                    name = product.name,
                    description = product.description,
                    price = product.price,
                    quantity = cartItem.quantity,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            recommendations = recommendations,
            loading = true,
            recommendationItemContent = { product ->
                ProductRecommendationCard(
                    product = product,
                    onAddClick = {}
                )
            }
        )
    }
}