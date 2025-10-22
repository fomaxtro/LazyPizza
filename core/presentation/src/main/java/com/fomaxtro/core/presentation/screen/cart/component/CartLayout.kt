package com.fomaxtro.core.presentation.screen.cart.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.fomaxtro.core.presentation.model.ProductUi
import com.fomaxtro.core.presentation.ui.ScreenType
import com.fomaxtro.core.presentation.ui.currentScreenType

@Composable
fun CartLayout(
    products: List<ProductUi>,
    productItemContent: @Composable (ProductUi) -> Unit,
    recommendations: List<ProductUi>,
    recommendationItemContent: @Composable (ProductUi) -> Unit,
    loading: Boolean,
    action: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    when (currentScreenType()) {
        ScreenType.MOBILE -> {
            CartPhone(
                products = products,
                productItemContent = productItemContent,
                recommendations = recommendations,
                recommendationItemContent = recommendationItemContent,
                loading = loading,
                action = action,
                modifier = modifier
            )
        }

        ScreenType.WIDE_SCREEN -> {
            CartWide(
                products = products,
                productItemContent = productItemContent,
                recommendations = recommendations,
                recommendationItemContent = recommendationItemContent,
                loading = loading,
                action = action,
                modifier = modifier
            )
        }
    }
}
