package com.fomaxtro.core.presentation.screen.product_details

import android.widget.Toast
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.fomaxtro.core.domain.model.ProductId
import com.fomaxtro.core.presentation.R
import com.fomaxtro.core.presentation.designsystem.button.LazyPizzaButton
import com.fomaxtro.core.presentation.designsystem.button.LazyPizzaNavigationIconButton
import com.fomaxtro.core.presentation.designsystem.modifier.shimmer
import com.fomaxtro.core.presentation.designsystem.theme.LazyPizzaTheme
import com.fomaxtro.core.presentation.designsystem.theme.fadeGradient
import com.fomaxtro.core.presentation.designsystem.theme.textSecondary
import com.fomaxtro.core.presentation.designsystem.top_bar.LazyPizzaTopAppBar
import com.fomaxtro.core.presentation.screen.product_details.component.ToppingItemLoader
import com.fomaxtro.core.presentation.screen.product_details.component.ToppingListItem
import com.fomaxtro.core.presentation.ui.Formatter
import com.fomaxtro.core.presentation.ui.ObserveAsEvents
import com.fomaxtro.core.presentation.util.ProductUiFactory
import com.fomaxtro.core.presentation.util.ToppingUiFactory
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ProductDetailsRoot(
    id: ProductId,
    viewModel: ProductDetailsViewModel = koinViewModel {
        parametersOf(id)
    }
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
        }
    }

    ProductDetailsScreen(
        onAction = viewModel::onAction,
        state = state
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProductDetailsScreen(
    onAction: (ProductDetailsAction) -> Unit = {},
    state: ProductDetailsState
) {
    val isInPreview = LocalInspectionMode.current

    Scaffold(
        topBar = {
            LazyPizzaTopAppBar(
                navigationIcon = {
                    LazyPizzaNavigationIconButton(
                        onClick = {}
                    )
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
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
                    ) {
                        if (isInPreview) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        color = MaterialTheme.colorScheme.background,
                                        shape = RoundedCornerShape(bottomEnd = 16.dp)
                                    )
                            )
                        } else {
                            AsyncImage(
                                model = state.product?.imageUrl,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        color = MaterialTheme.colorScheme.background,
                                        shape = RoundedCornerShape(bottomEnd = 16.dp)
                                    )
                            )
                        }
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
                            if (state.product != null) {
                                Text(
                                    text = state.product.name,
                                    style = MaterialTheme.typography.titleLarge
                                )

                                if (state.product.description != null) {
                                    Text(
                                        text = state.product.description,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.textSecondary
                                    )
                                }
                            } else {
                                Box(
                                    modifier = Modifier
                                        .height(24.dp)
                                        .width(124.dp)
                                        .shimmer()
                                )
                            }

                            if (state.product == null) {
                                Spacer(modifier = Modifier.height(8.dp))

                                Box(
                                    modifier = Modifier
                                        .height(16.dp)
                                        .fillMaxWidth()
                                        .shimmer()
                                )
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
                                if (state.isToppingsLoading) {
                                    repeat(6) {
                                        ToppingItemLoader(
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                } else {
                                    state.toppings.forEach { topping ->
                                        ToppingListItem(
                                            topping = topping,
                                            onClick = {
                                                onAction(
                                                    ProductDetailsAction.OnToppingQuantityChange(
                                                        topping = topping,
                                                        quantity = 1
                                                    )
                                                )
                                            },
                                            onQuantityChange = {
                                                onAction(
                                                    ProductDetailsAction.OnToppingQuantityChange(
                                                        topping = topping,
                                                        quantity = it
                                                    )
                                                )
                                            },
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                }
                            }

                            val offsetSpacing = 16.dp + 32.dp + ButtonDefaults.MinHeight

                            Spacer(modifier = Modifier.height(offsetSpacing))
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.fadeGradient)
                    .align(Alignment.BottomCenter)
            ) {
                LazyPizzaButton(
                    onClick = {},
                    text = stringResource(
                        R.string.add_to_cart_for,
                        Formatter.formatCurrency(state.totalPrice)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 16.dp
                        )
                        .padding(
                            top = 36.dp,
                            bottom = 16.dp
                        ),
                    enabled = state.canAddToCart
                )
            }
        }
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
                    ToppingUiFactory.create(
                        id = it.toLong()
                    )
                }
            ),
        )
    }
}