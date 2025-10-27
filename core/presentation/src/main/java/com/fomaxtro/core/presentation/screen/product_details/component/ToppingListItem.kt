package com.fomaxtro.core.presentation.screen.product_details.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.fomaxtro.core.presentation.component.QuantityPicker
import com.fomaxtro.core.presentation.designsystem.theme.LazyPizzaTheme
import com.fomaxtro.core.presentation.designsystem.theme.primary8
import com.fomaxtro.core.presentation.model.ToppingSelectionUi
import com.fomaxtro.core.presentation.ui.Formatters
import com.fomaxtro.core.presentation.util.ToppingUiFactory

@Composable
fun ToppingListItem(
    toppingSelection: ToppingSelectionUi,
    onClick: () -> Unit,
    onQuantityChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    BaseToppingItem(
        image = {
            AsyncImage(
                model = toppingSelection.topping.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary8,
                        shape = CircleShape
                    )
                    .padding(4.dp)
            )
        },
        name = {
            Text(toppingSelection.topping.name)
        },
        action = {
            if (toppingSelection.quantity > 0) {
                QuantityPicker(
                    onQuantityChange = onQuantityChange,
                    quantity = toppingSelection.quantity
                )
            } else {
                Text(
                    text = Formatters.formatCurrency(toppingSelection.topping.price),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        },
        modifier = modifier
            .clickable(
                onClick = onClick,
                indication = null,
                interactionSource = null
            ),
        selected = toppingSelection.quantity > 0
    )
}

@Preview(showBackground = true)
@Composable
private fun IngredientListItemPreview() {
    LazyPizzaTheme {
        ToppingListItem(
            toppingSelection = ToppingSelectionUi(
                topping = ToppingUiFactory.create(
                    id = 1
                ),
                quantity = 1
            ),
            onClick = {},
            onQuantityChange = {},
            modifier = Modifier.padding(8.dp)
        )
    }
}