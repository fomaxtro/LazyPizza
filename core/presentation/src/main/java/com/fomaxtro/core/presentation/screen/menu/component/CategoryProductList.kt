package com.fomaxtro.core.presentation.screen.menu.component

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
import com.fomaxtro.core.presentation.designsystem.theme.label2Semibold
import com.fomaxtro.core.presentation.designsystem.theme.textSecondary
import com.fomaxtro.core.presentation.model.ProductUi
import com.fomaxtro.core.presentation.ui.ScreenType
import com.fomaxtro.core.presentation.util.toDisplayName

fun LazyListScope.categoryProductList(
    category: ProductCategory,
    loading: Boolean,
    screenType: ScreenType,
    itemContent: @Composable (ProductUi) -> Unit,
    items: List<ProductUi>?
) {
    if (loading || (items != null && items.isNotEmpty())) {
        item {
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = category
                    .toDisplayName()
                    .asString(),
                style = MaterialTheme.typography.label2Semibold,
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
                            itemContent(item)
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
                                    itemContent(item)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
