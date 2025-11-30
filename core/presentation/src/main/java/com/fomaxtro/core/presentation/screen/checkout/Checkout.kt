package com.fomaxtro.core.presentation.screen.checkout

import android.widget.Toast
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fomaxtro.core.presentation.R
import com.fomaxtro.core.presentation.component.DateTimePicker
import com.fomaxtro.core.presentation.component.ProductListItem
import com.fomaxtro.core.presentation.component.ProductRecommendationCard
import com.fomaxtro.core.presentation.component.ProductRecommendationCardLoader
import com.fomaxtro.core.presentation.component.TopBarSheetSurface
import com.fomaxtro.core.presentation.designsystem.button.LazyPizzaButton
import com.fomaxtro.core.presentation.designsystem.text_field.LazyPizzaOutlinedFormTextField
import com.fomaxtro.core.presentation.designsystem.theme.LazyPizzaTheme
import com.fomaxtro.core.presentation.designsystem.top_bar.LazyPizzaCenteredAlignedTopAppBar
import com.fomaxtro.core.presentation.model.CartItemUi
import com.fomaxtro.core.presentation.screen.checkout.component.CheckoutLayout
import com.fomaxtro.core.presentation.screen.checkout.component.OutlinedRadioButton
import com.fomaxtro.core.presentation.screen.checkout.model.PickupOption
import com.fomaxtro.core.presentation.ui.Formatters
import com.fomaxtro.core.presentation.ui.ObserveAsEvents
import com.fomaxtro.core.presentation.ui.Resource
import com.fomaxtro.core.presentation.ui.getOrDefault
import com.fomaxtro.core.presentation.util.ProductUiFactory
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun CheckoutRoot(
    onNavigateBack: () -> Unit,
    onNavigateToOrderConfirmation: (orderId: Long, pickupTimeUtc: Long) -> Unit,
    viewModel: CheckoutViewModel
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    val context = LocalContext.current

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is CheckoutEvent.ShowMessage -> {
                snackbarHostState.showSnackbar(
                    message = event.message.asString(context)
                )
            }

            is CheckoutEvent.ShowSystemMessage -> {
                Toast.makeText(
                    context,
                    event.message.asString(context),
                    Toast.LENGTH_LONG
                ).show()
            }

            is CheckoutEvent.NavigateToOrderConfirmation -> {
                onNavigateToOrderConfirmation(event.orderId, event.pickupTimeUtc)
            }
        }
    }

    CheckoutScreen(
        state = state,
        onAction = { action ->
            when (action) {
                CheckoutAction.OnNavigateBackClick -> onNavigateBack()
                else -> viewModel.onAction(action)
            }
        },
        snackbarHostState = snackbarHostState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CheckoutScreen(
    state: CheckoutState,
    onAction: (CheckoutAction) -> Unit = {},
    snackbarHostState: SnackbarHostState
) {
    val focusManager = LocalFocusManager.current

    if (state.isDateTimePickerDialogVisible) {
        DateTimePicker(
            onDismissRequest = {
                onAction(CheckoutAction.OnPickupTimeDialogDismiss)
            },
            onDateTimeSelected = {
                onAction(CheckoutAction.OnPickupDateTimeSelected(it))
            },
            initialUtcMillis = state.pickupTime.toEpochMilli(),
            error = state.pickupTimeError
        )
    }

    Scaffold(
        topBar = {
            TopBarSheetSurface {
                LazyPizzaCenteredAlignedTopAppBar(
                    title = stringResource(R.string.order_checkout),
                    onNavigateBackClick = {
                        onAction(CheckoutAction.OnNavigateBackClick)
                    },
                    modifier = Modifier.padding(horizontal = 10.dp),
                    containerColor = MaterialTheme.colorScheme.surface
                )
            }
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
        modifier = Modifier.pointerInput(Unit) {
            detectTapGestures {
                focusManager.clearFocus()
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
                    when (state.pickupOption) {
                        PickupOption.EARLIEST -> DateTimeFormatter.ofPattern("HH:mm")
                        PickupOption.SCHEDULED -> Formatters.pickupTimeFormatter
                    }
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
                    onValueChange = {
                        onAction(CheckoutAction.OnCommentsChange(it))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(92.dp),
                    shape = RoundedCornerShape(24.dp),
                    placeholder = stringResource(R.string.add_comment)
                )
            },
            totalPrice = state.totalPrice,
            action = {
                LazyPizzaButton(
                    onClick = {
                        onAction(CheckoutAction.OnPlaceOrderClick)
                    },
                    text = stringResource(R.string.place_order),
                    loading = state.isSubmitting
                )
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFB61010)
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
            ),
            snackbarHostState = SnackbarHostState()
        )
    }
}