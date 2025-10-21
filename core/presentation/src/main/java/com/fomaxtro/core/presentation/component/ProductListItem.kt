package com.fomaxtro.core.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.fomaxtro.core.presentation.R
import com.fomaxtro.core.presentation.designsystem.button.LazyPizzaIconButton
import com.fomaxtro.core.presentation.designsystem.button.LazyPizzaOutlinedButton
import com.fomaxtro.core.presentation.designsystem.theme.AppIcons
import com.fomaxtro.core.presentation.designsystem.theme.LazyPizzaTheme
import com.fomaxtro.core.presentation.designsystem.theme.body1Medium
import com.fomaxtro.core.presentation.designsystem.theme.body3Regular
import com.fomaxtro.core.presentation.designsystem.theme.textSecondary
import com.fomaxtro.core.presentation.designsystem.theme.title1SemiBold
import com.fomaxtro.core.presentation.screen.menu.component.PriceDetail
import com.fomaxtro.core.presentation.ui.Formatters
import com.fomaxtro.core.presentation.util.ProductUiFactory

@Composable
fun ProductListItem(
    imageUrl: String,
    name: String,
    description: String?,
    price: Double,
    quantity: Int,
    modifier: Modifier = Modifier,
    onAddClick: (() -> Unit)? = null,
    onDeleteClick: (() -> Unit)? = null,
    onQuantityChange: ((Int) -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    val isInPreview = LocalInspectionMode.current

    BaseProductListItem(
        image = {
            if (!isInPreview) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = name,
                    modifier = Modifier.fillMaxSize()
                )
            }
        },
        onClick = onClick,
        modifier = modifier
            .height(128.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.body1Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                if (quantity > 0 && onDeleteClick != null) {
                    LazyPizzaIconButton(
                        onClick = onDeleteClick
                    ) {
                        Icon(
                            imageVector = AppIcons.Outlined.Trash,
                            contentDescription = stringResource(R.string.delete)
                        )
                    }
                }
            }

            if (description != null) {
                Text(
                    text = description,
                    style = MaterialTheme.typography.body3Regular,
                    color = MaterialTheme.colorScheme.textSecondary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (quantity > 0) {
                QuantityPicker(
                    onQuantityChange = {
                        onQuantityChange?.invoke(it)
                    },
                    quantity = quantity
                )
            } else {
                Text(
                    text = Formatters.formatCurrency(price),
                    style = MaterialTheme.typography.title1SemiBold
                )
            }

            if (onAddClick != null && quantity == 0) {
                LazyPizzaOutlinedButton(
                    onClick = onAddClick,
                    text = stringResource(R.string.add_to_cart)
                )
            } else if (quantity > 0) {
                PriceDetail(
                    price = price,
                    quantity = quantity
                )
            }
        }
    }
}

@Preview
@Composable
private fun ProductListItemPreview() {
    val product = ProductUiFactory.create(
        id = 1,
        quantity = 0,
        description = "Cream sauce, mozzarella, mushrooms, truffle oil, parmesan",
        price = 13.444
    )

    LazyPizzaTheme {
        ProductListItem(
            imageUrl = product.imageUrl,
            name = product.name,
            description = product.description,
            price = product.price,
            quantity = product.quantity,
            modifier = Modifier.fillMaxWidth(),
            onClick = {},
            onDeleteClick = {}
        )
    }
}