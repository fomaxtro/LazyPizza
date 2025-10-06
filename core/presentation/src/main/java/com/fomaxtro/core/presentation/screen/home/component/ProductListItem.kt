package com.fomaxtro.core.presentation.screen.home.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.fomaxtro.core.presentation.R
import com.fomaxtro.core.presentation.designsystem.button.LazyPizzaIconButton
import com.fomaxtro.core.presentation.designsystem.button.LazyPizzaOutlinedButton
import com.fomaxtro.core.presentation.designsystem.theme.AppIcons
import com.fomaxtro.core.presentation.designsystem.theme.LazyPizzaTheme
import com.fomaxtro.core.presentation.designsystem.theme.surfaceHighest
import com.fomaxtro.core.presentation.designsystem.theme.textSecondary
import com.fomaxtro.core.presentation.screen.home.model.ProductUi
import com.fomaxtro.core.presentation.screen.home.util.ProductUiFactory
import com.fomaxtro.core.presentation.ui.Formatter

@Composable
fun ProductListItem(
    product: ProductUi,
    modifier: Modifier = Modifier,
    onAddClick: (() -> Unit)? = null,
    onDeleteClick: (() -> Unit)? = null,
    onQuantityChange: ((Int) -> Unit)? = null
) {
    val isInPreview = LocalInspectionMode.current

    Card(
        modifier = modifier
            .height(IntrinsicSize.Max)
            .dropShadow(
                shape = CardDefaults.shape,
                shadow = Shadow(
                    color = Color(0xFF03131F).copy(alpha = 0.06f),
                    radius = 16.dp
                )
            ),
        border = BorderStroke(
            color = MaterialTheme.colorScheme.surface,
            width = 1.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isInPreview) {
                Box(
                    modifier = Modifier
                        .size(122.dp)
                        .background(MaterialTheme.colorScheme.surfaceHighest)
                )
            } else {
                AsyncImage(
                    model = product.imagUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(120.dp)
                        .background(MaterialTheme.colorScheme.surfaceHighest)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .padding(
                        horizontal = 16.dp,
                        vertical = 12.dp
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalAlignment = Alignment.Top
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = product.name,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        if (product.description != null) {
                            Text(
                                text = product.description,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.textSecondary,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    if (product.quantity > 0 && onDeleteClick != null) {
                        LazyPizzaIconButton(
                            onClick = onDeleteClick,
                            colors = IconButtonDefaults.iconButtonColors(
                                contentColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Icon(
                                imageVector = AppIcons.Outlined.Trash,
                                contentDescription = stringResource(R.string.delete)
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (product.quantity > 0) {
                        QuantityPicker(
                            onQuantityChange = {
                                onQuantityChange?.invoke(it)
                            },
                            quantity = product.quantity
                        )
                    } else {
                        Text(
                            text = Formatter.formatCurrency(product.price),
                            style = MaterialTheme.typography.titleLarge
                        )
                    }

                    if (onAddClick != null && product.quantity == 0) {
                        LazyPizzaOutlinedButton(
                            onClick = onAddClick,
                            text = stringResource(R.string.add_to_cart)
                        )
                    } else if (product.quantity > 0) {
                        PriceDetail(
                            price = product.price,
                            quantity = product.quantity
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun ProductListItemPreview() {
    LazyPizzaTheme {
        ProductListItem(
            product = ProductUiFactory.create(
                id = 1,
                quantity = 1,
                description = "Cream sauce, mozzarella, mushrooms, truffle oil, parmesan",
                price = 13.444
            ),
            modifier = Modifier.fillMaxWidth(),
            onAddClick = {},
            onDeleteClick = {}
        )
    }
}