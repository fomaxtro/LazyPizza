package com.fomaxtro.core.presentation.screen.product_details.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fomaxtro.core.presentation.R
import com.fomaxtro.core.presentation.component.GradientFadeBox
import com.fomaxtro.core.presentation.component.GradientFadeBoxDefaults
import com.fomaxtro.core.presentation.designsystem.button.LazyPizzaButton
import com.fomaxtro.core.presentation.designsystem.theme.LazyPizzaTheme
import com.fomaxtro.core.presentation.designsystem.theme.textSecondary
import com.fomaxtro.core.presentation.model.ToppingSelectionUi
import com.fomaxtro.core.presentation.util.ToppingUiFactory

@Composable
fun ProductDetailsPhone(
    image: @Composable () -> Unit,
    title: @Composable () -> Unit,
    subtitle: @Composable () -> Unit,
    action: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    itemContent: @Composable (ToppingSelectionUi) -> Unit,
    items: List<ToppingSelectionUi>,
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(3f / 2f)
                        .background(MaterialTheme.colorScheme.surface)
                        .background(
                            color = MaterialTheme.colorScheme.background,
                            shape = RoundedCornerShape(bottomEnd = 16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    image()
                }
            }

            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    shape = RoundedCornerShape(topStart = 16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(top = 20.dp)
                    ) {
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

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = stringResource(R.string.add_extra_toppings),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.textSecondary
                        )

                        Spacer(modifier = Modifier.height(7.dp))

                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            maxItemsInEachRow = 3,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            if (loading) {
                                repeat(6) {
                                    ToppingItemLoader(
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            } else {
                                items.forEach { item ->
                                    Box(
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        itemContent(item)
                                    }
                                }
                            }
                        }


                        Spacer(modifier = Modifier.height(GradientFadeBoxDefaults.offsetSpacing))
                    }
                }
            }
        }

        GradientFadeBox(
            modifier = Modifier.align(Alignment.BottomCenter),
            content = action
        )
    }
}

@Preview
@Composable
private fun ProductDetailsPhonePreview() {
    LazyPizzaTheme {
        ProductDetailsPhone(
            image = {},
            title = { Text("Title") },
            subtitle = { Text("Subtitle") },
            items = (1..6).map {
                ToppingSelectionUi(
                    topping = ToppingUiFactory.create(
                        id = it.toLong()
                    )
                )
            },
            itemContent = {
                ToppingListItem(
                    toppingSelection = it,
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