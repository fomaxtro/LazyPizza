package com.fomaxtro.core.presentation.screen.product_details.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fomaxtro.core.presentation.R
import com.fomaxtro.core.presentation.designsystem.button.LazyPizzaButton
import com.fomaxtro.core.presentation.designsystem.theme.LazyPizzaTheme
import com.fomaxtro.core.presentation.designsystem.theme.textSecondary
import com.fomaxtro.core.presentation.model.ToppingUi
import com.fomaxtro.core.presentation.util.ToppingUiFactory

@Composable
fun ProductDetailsWide(
    image: @Composable () -> Unit,
    title: @Composable () -> Unit,
    subtitle: @Composable () -> Unit,
    action: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    itemContent: @Composable (ToppingUi) -> Unit,
    items: List<ToppingUi>
) {
    Row(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier.aspectRatio(3f / 2f)
            ) {
                image()
            }

            Spacer(modifier = Modifier.height(16.dp))

            CompositionLocalProvider(
                LocalTextStyle provides MaterialTheme.typography.titleLarge
            ) {
                title()
            }

            Spacer(modifier = Modifier.height(4.dp))

            CompositionLocalProvider(
                LocalTextStyle provides MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.textSecondary
                )
            ) {
                subtitle()
            }
        }

        Card(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 58.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            shape = RoundedCornerShape(
                topStart = 16.dp,
                bottomStart = 16.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.add_extra_toppings),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.textSecondary
                )

                Spacer(modifier = Modifier.height(7.dp))

                LazyVerticalGrid(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (loading) {
                        items(9) {
                            ToppingItemLoader(
                                modifier = Modifier.weight(1f)
                            )
                        }
                    } else {
                        items(items, key = { it.id }) { product ->
                            Box(
                                modifier = Modifier.weight(1f)
                            ) {
                                itemContent(product)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                action()
            }
        }
    }
}

@Preview(device = "id:pixel_tablet", showBackground = true)
@Composable
private fun ProductDetailsWidePreview() {
    LazyPizzaTheme {
        ProductDetailsWide(
            image = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.primary)
                )
            },
            title = { Text("Margherita") },
            subtitle = { Text("Tomato sauce, Mozzarella, Fresh basil, Olive oil") },
            items = (1..15).map {
                ToppingUiFactory.create(
                    id = it.toLong()
                )
            },
            itemContent = { topping ->
                ToppingListItem(
                    topping = topping,
                    onClick = {},
                    onQuantityChange = {},
                    modifier = Modifier.fillMaxWidth()
                )
            },
            action = {
                LazyPizzaButton(
                    onClick = {},
                    text = "Add to cart for $12.99",
                    modifier = Modifier.fillMaxWidth()
                )
            }
        )
    }
}