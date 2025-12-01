package com.fomaxtro.core.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.AsyncImage
import com.fomaxtro.core.presentation.R
import com.fomaxtro.core.presentation.designsystem.button.LazyPizzaOutlinedIconButton
import com.fomaxtro.core.presentation.designsystem.theme.AppIcons
import com.fomaxtro.core.presentation.designsystem.theme.LazyPizzaTheme
import com.fomaxtro.core.presentation.designsystem.theme.body1Regular
import com.fomaxtro.core.presentation.designsystem.theme.textSecondary
import com.fomaxtro.core.presentation.designsystem.theme.title1SemiBold
import com.fomaxtro.core.presentation.model.ProductUi
import com.fomaxtro.core.presentation.ui.Formatters
import com.fomaxtro.core.presentation.util.ProductUiFactory

@Composable
fun ProductRecommendationCard(
    product: ProductUi,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isInPreview = LocalInspectionMode.current

    BaseProductRecommendationCard(
        image = {
            if (!isInPreview) {
                AsyncImage(
                    model = product.imageUrl,
                    contentDescription = product.name,
                    modifier = Modifier.fillMaxSize()
                )
            }
        },
        modifier = modifier
    ) {
        Text(
            text = product.name,
            style = MaterialTheme.typography.body1Regular,
            color = MaterialTheme.colorScheme.textSecondary
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = Formatters.formatCurrency(product.price),
                style = MaterialTheme.typography.title1SemiBold
            )

            LazyPizzaOutlinedIconButton(
                onClick = onAddClick
            ) {
                Icon(
                    imageVector = AppIcons.Filled.Plus,
                    contentDescription = stringResource(R.string.add)
                )
            }
        }
    }
}

@Preview
@Composable
private fun ProductRecommendationCardPreview() {
    LazyPizzaTheme {
        ProductRecommendationCard(
            product = ProductUiFactory.create(
                id = 1
            ),
            onAddClick = {}
        )
    }
}