package com.fomaxtro.core.presentation.screen.menu

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fomaxtro.core.domain.model.ProductCategory
import com.fomaxtro.core.presentation.R
import com.fomaxtro.core.presentation.component.ProductListItem
import com.fomaxtro.core.presentation.designsystem.text_field.LazyPizzaOutlinedTextField
import com.fomaxtro.core.presentation.designsystem.theme.AppIcons
import com.fomaxtro.core.presentation.designsystem.theme.LazyPizzaTheme
import com.fomaxtro.core.presentation.designsystem.theme.body3Medium
import com.fomaxtro.core.presentation.designsystem.theme.textPrimary
import com.fomaxtro.core.presentation.screen.menu.component.categoryProductList
import com.fomaxtro.core.presentation.ui.ObserveAsEvents
import com.fomaxtro.core.presentation.ui.ScreenType
import com.fomaxtro.core.presentation.ui.currentScreenType
import com.fomaxtro.core.presentation.util.toDisplayName
import org.koin.androidx.compose.koinViewModel

@Composable
fun MenuRoot(
    onProductClick: (productId: Long) -> Unit,
    viewModel: MenuViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is MenuEvent.ShowSystemMessage -> {
                Toast.makeText(
                    context,
                    event.message.asString(context),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    MenuScreen(
        onAction = { action ->
            when (action) {
                is MenuAction.OnProductClick -> onProductClick(action.product.id)
                else -> viewModel.onAction(action)
            }
        },
        state = state
    )
}

@Composable
private fun MenuScreen(
    onAction: (MenuAction) -> Unit = {},
    state: MenuState
) {
    val screenType = currentScreenType()
    val productCategories = listOf(
        ProductCategory.PIZZA,
        ProductCategory.DRINKS,
        ProductCategory.SAUCES,
        ProductCategory.ICE_CREAM
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .imePadding()
    ) {
        item {
            val bannerImage = when (screenType) {
                ScreenType.MOBILE -> painterResource(R.drawable.banner)
                ScreenType.WIDE_SCREEN -> painterResource(R.drawable.banner_wide)
            }

            Image(
                painter = bannerImage,
                contentDescription = stringResource(R.string.banner),
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        stickyHeader {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                LazyPizzaOutlinedTextField(
                    value = state.search,
                    onValueChange = {
                        onAction(MenuAction.OnSearchChange(it))
                    },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(
                            imageVector = AppIcons.Outlined.Search,
                            contentDescription = stringResource(R.string.search),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    placeholder = stringResource(R.string.search_food)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    productCategories.forEach { productCategory ->
                        FilterChip(
                            selected = state.selectedCategory == productCategory,
                            onClick = {
                                onAction(MenuAction.OnProductCategoryToggle(productCategory))
                            },
                            label = {
                                Text(
                                    text = productCategory
                                        .toDisplayName()
                                        .asString(),
                                    style = MaterialTheme.typography.body3Medium
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
        }

        categoryProductList(
            category = ProductCategory.PIZZA,
            items = state.cartItems[ProductCategory.PIZZA],
            loading = state.isLoading,
            screenType = screenType,
            itemContent = { cartItem ->
                val product = cartItem.product

                ProductListItem(
                    imageUrl = product.imageUrl,
                    name = product.name,
                    description = product.description,
                    price = product.price,
                    quantity = cartItem.quantity,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        onAction(MenuAction.OnProductClick(product))
                    }
                )
            }
        )

        productCategories.drop(1).forEach { productCategory ->
            categoryProductList(
                category = productCategory,
                items = state.cartItems[productCategory],
                loading = state.isLoading,
                screenType = screenType,
                itemContent = { cartItem ->
                    val product = cartItem.product

                    ProductListItem(
                        imageUrl = product.imageUrl,
                        name = product.name,
                        description = product.description,
                        price = product.price,
                        quantity = cartItem.quantity,
                        modifier = Modifier.fillMaxWidth(),
                        onAddClick = {
                            onAction(
                                MenuAction.OnCartItemQuantityChange(
                                    productId = cartItem.product.id,
                                    quantity = 1
                                )
                            )
                        },
                        onDeleteClick = {
                            onAction(
                                MenuAction.OnCartItemQuantityChange(
                                    productId = cartItem.product.id,
                                    quantity = 0
                                )
                            )
                        },
                        onQuantityChange = {
                            onAction(
                                MenuAction.OnCartItemQuantityChange(
                                    productId = cartItem.product.id,
                                    quantity = it
                                )
                            )
                        }
                    )
                }
            )
        }
    }
}

@Preview
@Composable
private fun MenuScreenPreview() {
    LazyPizzaTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            MenuScreen(
                state = MenuState()
            )
        }
    }
}