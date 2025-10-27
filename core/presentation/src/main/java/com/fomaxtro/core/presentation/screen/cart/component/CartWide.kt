package com.fomaxtro.core.presentation.screen.cart.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fomaxtro.core.presentation.R
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
fun CartWide(
    cartItems: List<CartItemUi>,
    productItemContent: @Composable LazyItemScope.(CartItemUi) -> Unit,
    recommendations: List<ProductUi>,
    recommendationItemContent: @Composable LazyItemScope.(ProductUi) -> Unit,
    loading: Boolean,
    action: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp)
        ) {
            if (loading) {
                items(3) {
                    ProductListItemLoader(
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                }
            } else {
                items(cartItems, key = { it.id }) { product ->
                    productItemContent(product)

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        Card(
            modifier = Modifier.weight(1f),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .padding(start = 16.dp)
            ) {
                Text(
                    text = stringResource(R.string.order_recommendation).uppercase(),
                    style = MaterialTheme.typography.label2Semibold,
                    color = MaterialTheme.colorScheme.textSecondary
                )

                Spacer(modifier = Modifier.height(8.dp))

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

                Spacer(modifier = Modifier.height(20.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 16.dp)
                ) {
                    action()
                }
            }
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_tablet")
@Composable
private fun CartWidePreview() {
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
            id = id.toString(),
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
        CartWide(
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
            loading = false,
            recommendationItemContent = { product ->
                ProductRecommendationCard(
                    product = product,
                    onAddClick = {}
                )
            }
        )
    }
}