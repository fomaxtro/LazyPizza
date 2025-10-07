package com.fomaxtro.core.presentation.screen.home.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fomaxtro.core.domain.model.ProductCategory
import com.fomaxtro.core.presentation.R
import com.fomaxtro.core.presentation.designsystem.theme.textSecondary
import com.fomaxtro.core.presentation.screen.home.util.toDisplayName

fun LazyListScope.productsLoader(
    productCategories: List<ProductCategory>
) {
    item {
        Text(
            text = stringResource(R.string.pizza),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.textSecondary
        )
    }

    items(3) {
        Spacer(modifier = Modifier.height(8.dp))

        ProductListItemLoader(
            modifier = Modifier.fillMaxWidth()
        )
    }

    item {
        Spacer(modifier = Modifier.height(16.dp))
    }

    productCategories.drop(1).forEach { productCategory ->
        item {
            Text(
                text = productCategory
                    .toDisplayName()
                    .asString(),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.textSecondary
            )
        }

        items(3) {
            Spacer(modifier = Modifier.height(8.dp))

            ProductListItemLoader(
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
