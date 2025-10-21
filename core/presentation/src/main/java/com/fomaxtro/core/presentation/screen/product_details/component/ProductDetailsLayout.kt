package com.fomaxtro.core.presentation.screen.product_details.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.fomaxtro.core.presentation.designsystem.button.LazyPizzaButton
import com.fomaxtro.core.presentation.designsystem.theme.LazyPizzaTheme
import com.fomaxtro.core.presentation.model.ToppingUi
import com.fomaxtro.core.presentation.ui.ScreenType
import com.fomaxtro.core.presentation.ui.currentScreenType
import com.fomaxtro.core.presentation.util.ToppingUiFactory

@Composable
fun ProductDetailsLayout(
    image: @Composable () -> Unit,
    title: @Composable () -> Unit,
    subtitle: @Composable () -> Unit,
    action: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    itemContent: @Composable (ToppingUi) -> Unit,
    items: List<ToppingUi>
) {
    val screenType = currentScreenType()

    when (screenType) {
        ScreenType.MOBILE -> {
            ProductDetailsPhone(
                image = image,
                title = title,
                subtitle = subtitle,
                action = action,
                modifier = modifier,
                loading = loading,
                itemContent = itemContent,
                items = items
            )

        }

        ScreenType.WIDE_SCREEN -> {
            ProductDetailsWide(
                image = image,
                title = title,
                subtitle = subtitle,
                action = action,
                modifier = modifier,
                loading = loading,
                itemContent = itemContent,
                items = items
            )
        }
    }
}

@Preview(device = "id:pixel_9_pro_xl")
@Composable
private fun ProductDetailsLayoutPreview() {
    LazyPizzaTheme {
        ProductDetailsLayout(
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