package com.fomaxtro.core.presentation.screen.checkout.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fomaxtro.core.presentation.R
import com.fomaxtro.core.presentation.component.GradientFadeBox
import com.fomaxtro.core.presentation.component.ProductListItem
import com.fomaxtro.core.presentation.component.ProductRecommendationCard
import com.fomaxtro.core.presentation.component.ProductRecommendationCardLoader
import com.fomaxtro.core.presentation.designsystem.button.LazyPizzaButton
import com.fomaxtro.core.presentation.designsystem.button.LazyPizzaOutlinedIconButton
import com.fomaxtro.core.presentation.designsystem.theme.LazyPizzaTheme
import com.fomaxtro.core.presentation.designsystem.theme.label1Medium
import com.fomaxtro.core.presentation.designsystem.theme.label1Semibold
import com.fomaxtro.core.presentation.designsystem.theme.label2Semibold
import com.fomaxtro.core.presentation.designsystem.theme.textSecondary
import com.fomaxtro.core.presentation.model.CartItemUi
import com.fomaxtro.core.presentation.model.ProductUi
import com.fomaxtro.core.presentation.ui.Formatters
import com.fomaxtro.core.presentation.ui.ScreenType
import com.fomaxtro.core.presentation.ui.currentScreenType
import com.fomaxtro.core.presentation.util.ProductUiFactory
import java.util.Locale

@Composable
fun CheckoutLayout(
    pickupOptions: @Composable () -> Unit,
    pickupTime: String,
    cartItems: List<CartItemUi>,
    cartItemBuilder: @Composable (CartItemUi) -> Unit,
    productRecommendationsLoading: Boolean,
    productRecommendationsLoader: @Composable () -> Unit,
    productRecommendations: List<ProductUi>,
    productRecommendationBuilder: @Composable (ProductUi) -> Unit,
    comments: @Composable () -> Unit,
    totalPrice: Double,
    modifier: Modifier = Modifier
) {
    var isOrderDetailsExpanded by rememberSaveable {
        mutableStateOf(false)
    }
    val screenType = currentScreenType()
    val labelTextStyle = MaterialTheme.typography.label2Semibold
    val labelTextColor = MaterialTheme.colorScheme.textSecondary
    var actionHeightPx by rememberSaveable {
        mutableIntStateOf(0)
    }
    val actionHeightDp = with(LocalDensity.current) { actionHeightPx.toDp() }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            item {
                Text(
                    text = stringResource(R.string.pickup_time).uppercase(),
                    style = labelTextStyle,
                    color = labelTextColor
                )
            }

            item {
                Spacer(modifier = Modifier.height(12.dp))
                pickupOptions()
            }

            item {
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.earliest_pickup_time).uppercase(Locale.getDefault()),
                        style = labelTextStyle,
                        color = labelTextColor
                    )

                    Text(
                        text = pickupTime,
                        style = labelTextStyle
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                HorizontalDivider()
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            isOrderDetailsExpanded = !isOrderDetailsExpanded
                        }
                        .padding(vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.order_details).uppercase(),
                        style = labelTextStyle,
                        color = labelTextColor
                    )

                    LazyPizzaOutlinedIconButton(
                        onClick = {
                            isOrderDetailsExpanded = !isOrderDetailsExpanded
                        },
                        color = labelTextColor
                    ) {
                        if (isOrderDetailsExpanded) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowUp,
                                contentDescription = stringResource(R.string.hide_order_details)
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = stringResource(R.string.show_order_details)
                            )
                        }
                    }
                }
            }

            item {
                AnimatedVisibility(
                    visible = isOrderDetailsExpanded,
                    enter = fadeIn() + expandVertically(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    when (screenType) {
                        ScreenType.MOBILE -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                cartItems.forEach {
                                    cartItemBuilder(it)
                                }
                            }
                        }

                        ScreenType.WIDE_SCREEN -> {
                            FlowRow(
                                maxItemsInEachRow = 2,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                cartItems.forEach {
                                    Box(
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        cartItemBuilder(it)
                                    }
                                }

                                if (cartItems.isNotEmpty() && cartItems.size % 2 != 0) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
            }

            item {
                HorizontalDivider()
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.recommended_addons).uppercase(),
                    style = labelTextStyle,
                    color = labelTextColor
                )

                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = 8.dp,
                            bottom = 16.dp
                        ),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (productRecommendationsLoading) {
                        items(3) {
                            productRecommendationsLoader()
                        }
                    } else {
                        items(productRecommendations) {
                            productRecommendationBuilder(it)
                        }
                    }
                }
            }

            item {
                HorizontalDivider()

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.comments).uppercase(),
                    style = labelTextStyle,
                    color = labelTextColor
                )

                Spacer(modifier = Modifier.height(12.dp))

                comments()
            }

            item {
                Spacer(modifier = Modifier.height(actionHeightDp))
            }
        }

        GradientFadeBox(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .onGloballyPositioned {
                    actionHeightPx = it.size.height
                },
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 56.dp
            )
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth()
            ) {
                when (screenType) {
                    ScreenType.MOBILE -> {
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = stringResource(R.string.order_total).uppercase(Locale.getDefault()),
                                    style = MaterialTheme.typography.label1Medium,
                                    color = labelTextColor
                                )

                                Text(
                                    text = Formatters.formatCurrency(totalPrice),
                                    style = MaterialTheme.typography.label1Semibold
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            LazyPizzaButton(
                                onClick = {},
                                text = stringResource(R.string.place_order),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    ScreenType.WIDE_SCREEN -> {

                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CheckoutLayoutPreview() {
    val cartItems = (1..4).map {
        CartItemUi(
            id = it.toString(),
            product = ProductUiFactory.create(
                id = it.toLong()
            ),
            quantity = it
        )
    }

    LazyPizzaTheme {
        CheckoutLayout(
            pickupOptions = {
                OutlinedRadioButton(
                    onClick = {},
                    selected = true,
                    text = stringResource(R.string.earliest_available_time),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedRadioButton(
                    onClick = {},
                    selected = false,
                    text = stringResource(R.string.scheduled_time),
                    modifier = Modifier.fillMaxWidth()
                )
            },
            pickupTime = "12:15",
            cartItems = cartItems,
            cartItemBuilder = { cartItem ->
                ProductListItem(
                    imageUrl = cartItem.product.imageUrl,
                    name = cartItem.product.name,
                    description = cartItem.product.description,
                    price = cartItem.priceWithToppings,
                    quantity = cartItem.quantity,
                    onQuantityChange = {},
                    minQuantity = 1,
                    onDeleteClick = {}
                )
            },
            productRecommendationsLoading = false,
            productRecommendations = (1..3).map {
                ProductUiFactory.create(
                    id = it.toLong()
                )
            },
            productRecommendationsLoader = {
                ProductRecommendationCardLoader()
            },
            productRecommendationBuilder = { product ->
                ProductRecommendationCard(
                    product = product,
                    onAddClick = {}
                )
            },
            comments = {
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
            },
            totalPrice = cartItems.sumOf { it.totalPrice }
        )
    }
}