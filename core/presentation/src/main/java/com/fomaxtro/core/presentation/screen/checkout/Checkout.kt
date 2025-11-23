package com.fomaxtro.core.presentation.screen.checkout

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.fomaxtro.core.presentation.R
import com.fomaxtro.core.presentation.component.ProductListItem
import com.fomaxtro.core.presentation.component.ProductRecommendationCard
import com.fomaxtro.core.presentation.component.ProductRecommendationCardLoader
import com.fomaxtro.core.presentation.designsystem.text_field.LazyPizzaOutlinedFormTextField
import com.fomaxtro.core.presentation.designsystem.theme.LazyPizzaTheme
import com.fomaxtro.core.presentation.designsystem.theme.textPrimary
import com.fomaxtro.core.presentation.designsystem.top_bar.LazyPizzaCenteredAlignedTopAppBar
import com.fomaxtro.core.presentation.model.CartItemUi
import com.fomaxtro.core.presentation.screen.checkout.component.CheckoutLayout
import com.fomaxtro.core.presentation.screen.checkout.component.OutlinedRadioButton
import com.fomaxtro.core.presentation.screen.checkout.model.PickupOption
import com.fomaxtro.core.presentation.util.ProductUiFactory
import com.fomaxtro.core.presentation.util.Resource
import com.fomaxtro.core.presentation.util.getOrDefault
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CheckoutScreen(
    state: CheckoutState
) {
    val shape = RoundedCornerShape(
        topStart = 16.dp,
        topEnd = 16.dp
    )

    Scaffold(
        topBar = {
            LazyPizzaCenteredAlignedTopAppBar(
                title = stringResource(R.string.order_checkout),
                onNavigateBackClick = {},
                modifier = Modifier.padding(horizontal = 10.dp)
            )
        },
        modifier = Modifier
            .clip(shape)
            .dropShadow(
                shape = shape,
                shadow = Shadow(
                    color = MaterialTheme.colorScheme.textPrimary.copy(alpha = 0.04f),
                    radius = 16.dp,
                    offset = DpOffset(0.dp, (-4).dp)
                )
            )
    ) { innerPadding ->
        CheckoutLayout(
            pickupOptions = {
                OutlinedRadioButton(
                    onClick = {},
                    selected = state.pickupOption == PickupOption.EARLIEST,
                    text = stringResource(R.string.earliest_available_time),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedRadioButton(
                    onClick = {},
                    selected = state.pickupOption == PickupOption.SCHEDULED,
                    text = stringResource(R.string.scheduled_time),
                    modifier = Modifier.fillMaxWidth()
                )
            },
            pickupTime = state.pickupTime
                .atZone(ZoneId.systemDefault())
                .format(
                    DateTimeFormatter.ofPattern(
                        when (state.pickupOption) {
                            PickupOption.EARLIEST -> "HH:mm"
                            PickupOption.SCHEDULED -> "MMMM dd, HH:mm"
                        }
                    )
                ),
            cartItems = state.cartItems,
            cartItemBuilder = { cartItem ->
                val product = cartItem.product

                ProductListItem(
                    imageUrl = product.imageUrl,
                    name = product.name,
                    description = product.description,
                    price = cartItem.priceWithToppings,
                    quantity = cartItem.quantity,
                    modifier = Modifier.fillMaxWidth(),
                    onDeleteClick = {},
                    onQuantityChange = {},
                    minQuantity = 1
                )
            },
            productRecommendationsLoading = state.productRecommendations.isLoading,
            productRecommendationsLoader = {
                ProductRecommendationCardLoader()
            },
            productRecommendations = state.productRecommendations.getOrDefault(emptyList()),
            productRecommendationBuilder = {
                ProductRecommendationCard(
                    product = it,
                    onAddClick = {}
                )
            },
            comments = {
                LazyPizzaOutlinedFormTextField(
                    value = state.comments,
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    shape = RoundedCornerShape(24.dp),
                    placeholder = stringResource(R.string.add_comment)
                )
            },
            totalPrice = state.totalPrice,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
        )
    }
}

@Preview
@Composable
private fun CheckoutScreenPreview() {
    LazyPizzaTheme {
        CheckoutScreen(
            state = CheckoutState(
                pickupOption = PickupOption.EARLIEST,
                cartItems = (1..3).map {
                    CartItemUi(
                        id = it.toString(),
                        product = ProductUiFactory.create(
                            id = it.toLong()
                        ),
                        quantity = it
                    )
                },
                productRecommendations = Resource.Success(
                    data = (1..3).map {
                        ProductUiFactory.create(
                            id = it.toLong()
                        )
                    }
                )
            )
        )
    }
}