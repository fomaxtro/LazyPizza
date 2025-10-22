package com.fomaxtro.core.presentation.screen.cart.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fomaxtro.core.presentation.designsystem.modifier.shimmer
import com.fomaxtro.core.presentation.designsystem.theme.LazyPizzaTheme

@Composable
fun ProductRecommendationCardLoader(
    modifier: Modifier = Modifier
) {
    BaseProductRecommendationCard(
        image = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .shimmer()
            )
        },
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(fraction = 0.5f)
                .height(22.dp)
                .shimmer()
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(22.dp)
                .shimmer()
        )
    }
}

@Preview
@Composable
private fun ProductRecommendationCardLoaderPreview() {
    LazyPizzaTheme {
        ProductRecommendationCardLoader()
    }
}