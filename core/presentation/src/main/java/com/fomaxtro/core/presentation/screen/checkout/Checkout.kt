package com.fomaxtro.core.presentation.screen.checkout

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fomaxtro.core.presentation.R
import com.fomaxtro.core.presentation.component.ProductListItem
import com.fomaxtro.core.presentation.component.ProductRecommendationCard
import com.fomaxtro.core.presentation.component.ProductRecommendationCardLoader
import com.fomaxtro.core.presentation.designsystem.text_field.LazyPizzaOutlinedFormTextField
import com.fomaxtro.core.presentation.designsystem.theme.LazyPizzaTheme
import com.fomaxtro.core.presentation.designsystem.top_bar.LazyPizzaCenteredAlignedTopAppBar
import com.fomaxtro.core.presentation.model.CartItemUi
import com.fomaxtro.core.presentation.screen.checkout.component.CheckoutLayout
import com.fomaxtro.core.presentation.component.DateTimePicker
import com.fomaxtro.core.presentation.screen.checkout.component.OutlinedRadioButton
import com.fomaxtro.core.presentation.screen.checkout.model.PickupOption
import com.fomaxtro.core.presentation.util.ProductUiFactory
import com.fomaxtro.core.presentation.ui.Resource
import com.fomaxtro.core.presentation.ui.getOrDefault
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun CheckoutRoot(
    onNavigateBack: () -> Unit,
    viewModel: CheckoutViewModel
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    CheckoutScreen(
        state = state,
        onAction = { action ->
            when (action) {
                CheckoutAction.OnNavigateBackClick -> onNavigateBack()
                else -> viewModel.onAction(action)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CheckoutScreen(
    state: CheckoutState,
    onAction: (CheckoutAction) -> Unit = {}
) {
    val shape = RoundedCornerShape(
        topStart = 16.dp,
        topEnd = 16.dp
    )

    if (state.isDateTimePickerDialogVisible) {
        DateTimePicker(
            onDismissRequest = {
                onAction(CheckoutAction.OnPickupTimeDialogDismiss)
            },
            onDateTimeSelected = {
                onAction(CheckoutAction.OnPickupDateTimeSelected(it))
            },
            initialUtcMillis = state.pickupTime.toEpochMilli()
        )
    }

    Scaffold(
        topBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding(),
                color = MaterialTheme.colorScheme.surface,
                shape = shape
            ) {
                LazyPizzaCenteredAlignedTopAppBar(
                    title = stringResource(R.string.order_checkout),
                    onNavigateBackClick = {
                        onAction(CheckoutAction.OnNavigateBackClick)
                    },
                    modifier = Modifier.padding(horizontal = 10.dp),
                    containerColor = MaterialTheme.colorScheme.surface
                )
            }
        }
    ) { innerPadding ->
        CheckoutLayout(
            pickupOptions = {
                OutlinedRadioButton(
                    onClick = {
                        onAction(CheckoutAction.OnPickupTimeOptionSelected(PickupOption.EARLIEST))
                    },
                    selected = state.pickupOption == PickupOption.EARLIEST,
                    text = stringResource(R.string.earliest_available_time),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedRadioButton(
                    onClick = {
                        onAction(CheckoutAction.OnPickupTimeOptionSelected(PickupOption.SCHEDULED))
                    },
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
            cartItemsLoading = state.cartItems is Resource.Loading,
            cartItemsLoader = {},
            cartItems = state.cartItems.getOrDefault(emptyList()),
            cartItemBuilder = { cartItem ->
                val product = cartItem.product

                ProductListItem(
                    imageUrl = product.imageUrl,
                    name = product.name,
                    description = null,
                    price = cartItem.priceWithToppings,
                    quantity = cartItem.quantity,
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateItem(),
                    onDeleteClick = {
                        onAction(
                            CheckoutAction.OnCartItemQuantityChange(
                                cartItemId = cartItem.id,
                                quantity = 0
                            )
                        )
                    },
                    onQuantityChange = {
                        onAction(
                            CheckoutAction.OnCartItemQuantityChange(
                                cartItemId = cartItem.id,
                                quantity = it
                            )
                        )
                    },
                    minQuantity = 1
                )
            },
            productRecommendationsLoading = state.productRecommendations is Resource.Loading,
            productRecommendationsLoader = {
                ProductRecommendationCardLoader()
            },
            productRecommendations = state.productRecommendations.getOrDefault(emptyList()),
            productRecommendationBuilder = { product ->
                ProductRecommendationCard(
                    product = product,
                    onAddClick = {
                        onAction(
                            CheckoutAction.OnAddProductRecommendationClick(product.id)
                        )
                    },
                    modifier = Modifier.animateItem()
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
    val cartItems = (1..3).map {
        CartItemUi(
            id = it.toString(),
            product = ProductUiFactory.create(
                id = it.toLong()
            ),
            quantity = it
        )
    }

    LazyPizzaTheme {
        CheckoutScreen(
            state = CheckoutState(
                pickupOption = PickupOption.EARLIEST,
                cartItems = Resource.Success(cartItems),
                productRecommendations = Resource.Success(
                    data = (1..3).map {
                        ProductUiFactory.create(
                            id = it.toLong()
                        )
                    }
                ),
                isDateTimePickerDialogVisible = false
            )
        )
    }
}