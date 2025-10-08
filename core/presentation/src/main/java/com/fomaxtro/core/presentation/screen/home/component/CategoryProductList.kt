package com.fomaxtro.core.presentation.screen.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fomaxtro.core.domain.model.ProductCategory
import com.fomaxtro.core.presentation.designsystem.theme.textSecondary
import com.fomaxtro.core.presentation.screen.home.model.ProductUi
import com.fomaxtro.core.presentation.screen.home.util.toDisplayName
import com.fomaxtro.core.presentation.ui.ScreenType

fun LazyListScope.categoryProductList(
    category: ProductCategory,
    items: List<ProductUi>?,
    loading: Boolean,
    screenType: ScreenType,
    content: @Composable (ProductUi) -> Unit
) {
    if (loading || (items != null && items.isNotEmpty())) {
        item {
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = category
                    .toDisplayName()
                    .asString(),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.textSecondary
            )
        }

        when (screenType) {
            ScreenType.MOBILE -> {
                if (loading) {
                    items(3) {
                        Spacer(modifier = Modifier.height(8.dp))

                        ProductListItemLoader(
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                } else {
                    items(items!!, key = { it.id }) { item ->
                        Spacer(modifier = Modifier.height(8.dp))

                        Box(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            content(item)
                        }
                    }
                }
            }

            ScreenType.WIDE_SCREEN -> {
                item {
                    Spacer(modifier = Modifier.height(8.dp))

                    FlowRow(
                        maxItemsInEachRow = 2,
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (loading) {
                            repeat(4) {
                                ProductListItemLoader(
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        } else {
                            items!!.forEach { item ->
                                Box(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    content(item)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
