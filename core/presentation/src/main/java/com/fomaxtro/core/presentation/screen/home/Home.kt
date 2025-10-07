package com.fomaxtro.core.presentation.screen.home

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fomaxtro.core.domain.model.ProductCategory
import com.fomaxtro.core.presentation.R
import com.fomaxtro.core.presentation.designsystem.text_field.LazyPizzaOutlinedTextField
import com.fomaxtro.core.presentation.designsystem.theme.AppIcons
import com.fomaxtro.core.presentation.designsystem.theme.LazyPizzaTheme
import com.fomaxtro.core.presentation.designsystem.theme.textPrimary
import com.fomaxtro.core.presentation.designsystem.theme.textSecondary
import com.fomaxtro.core.presentation.screen.home.component.ProductListItem
import com.fomaxtro.core.presentation.screen.home.component.productsLoader
import com.fomaxtro.core.presentation.screen.home.util.ProductUiFactory
import com.fomaxtro.core.presentation.screen.home.util.toDisplayName
import com.fomaxtro.core.presentation.ui.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeRoot(
    viewModel: HomeViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is HomeEvent.ShowSystemMessage -> {
                Toast.makeText(
                    context,
                    event.message.asString(context),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    HomeScreen(
        onAction = viewModel::onAction,
        state = state
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    onAction: (HomeAction) -> Unit = {},
    state: HomeState
) {
    val isInPreview = LocalInspectionMode.current
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    val productCategories = listOf(
        ProductCategory.PIZZA,
        ProductCategory.DRINKS,
        ProductCategory.SAUCES,
        ProductCategory.ICE_CREAM
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_launcher_foreground),
                            contentDescription = stringResource(R.string.app_name),
                            modifier = Modifier.size(20.dp)
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        Text(
                            text = stringResource(R.string.app_name),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                actions = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = AppIcons.Filled.Phone,
                            contentDescription = stringResource(R.string.contact)
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        val contactPhoneNumber = stringResource(R.string.contact_phone_number)

                        Text(
                            text = contactPhoneNumber,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier
                                .clickable {
                                    if (!isInPreview) {
                                        val dialerIntent = Intent(Intent.ACTION_DIAL).apply {
                                            data = "tel:$contactPhoneNumber".toUri()
                                        }

                                        context.startActivity(dialerIntent)
                                    }
                                }
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        modifier = Modifier
            .pointerInput(Unit) {
                detectTapGestures {
                    focusManager.clearFocus()
                }
            }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .imePadding()
        ) {
            item {
                Image(
                    painter = painterResource(R.drawable.banner),
                    contentDescription = stringResource(R.string.banner),
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.FillWidth
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                LazyPizzaOutlinedTextField(
                    value = state.search,
                    onValueChange = {
                        onAction(HomeAction.OnSearchChange(it))
                    },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(
                            imageVector = AppIcons.Outlined.Search,
                            contentDescription = stringResource(R.string.search),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    placeholder = {
                        Text(stringResource(R.string.search_food))
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(12.dp))
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    productCategories.forEach { productCategory ->
                        FilterChip(
                            selected = state.selectedCategories.contains(productCategory),
                            onClick = {
                                onAction(HomeAction.OnProductCategoryToggle(productCategory))
                            },
                            label = {
                                Text(
                                    text = productCategory
                                        .toDisplayName()
                                        .asString(),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                                labelColor = MaterialTheme.colorScheme.textPrimary
                            )
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(12.dp))
            }

            if (state.isLoading) {
                productsLoader(productCategories)
            } else {
                state.products[ProductCategory.PIZZA]?.let { pizzas ->
                    item {
                        Text(
                            text = stringResource(R.string.pizza),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.textSecondary
                        )
                    }

                    items(pizzas, key = { it.id }) { product ->
                        Spacer(modifier = Modifier.height(8.dp))

                        ProductListItem(
                            product = product
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                productCategories.drop(1).forEach { productCategory ->
                    state.products[productCategory]?.let { products ->
                        item {
                            Text(
                                text = productCategory
                                    .toDisplayName()
                                    .asString(),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.textSecondary
                            )
                        }

                        items(products, key = { it.id }) { product ->
                            Spacer(modifier = Modifier.height(8.dp))

                            ProductListItem(
                                product = product,
                                onAddClick = {},
                                onDeleteClick = {},
                                onQuantityChange = {}
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    LazyPizzaTheme {
        HomeScreen(
            state = HomeState(
                selectedCategories = setOf(
                    ProductCategory.PIZZA,
                    ProductCategory.SAUCES
                ),
                products = mapOf(
                    ProductCategory.PIZZA to (1..5).map {
                        ProductUiFactory.create(
                            id = it.toLong(),
                            name = "Pizza $it",
                            description = "This is a tasty pizza",
                            category = ProductCategory.PIZZA
                        )
                    },
                    ProductCategory.DRINKS to (1..3).map {
                        ProductUiFactory.create(
                            id = (it + 10).toLong(),
                            name = "Drink $it",
                            category = ProductCategory.DRINKS
                        )
                    },
                    ProductCategory.SAUCES to (1..2).map {
                        ProductUiFactory.create(
                            id = (it + 20).toLong(),
                            name = "Sauce $it",
                            category = ProductCategory.SAUCES
                        )
                    },
                    ProductCategory.ICE_CREAM to (1..2).map {
                        ProductUiFactory.create(
                            id = (it + 30).toLong(),
                            name = "Ice Cream $it",
                            category = ProductCategory.ICE_CREAM
                        )
                    }
                ),
                isLoading = true
            )
        )
    }
}