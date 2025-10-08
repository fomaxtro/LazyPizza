package com.fomaxtro.core.presentation.screen.product_details.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fomaxtro.core.presentation.designsystem.modifier.shimmer
import com.fomaxtro.core.presentation.designsystem.theme.LazyPizzaTheme

@Composable
fun ToppingItemLoader(
    modifier: Modifier = Modifier
) {
    BaseToppingItem(
        image = {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .shimmer()
            )
        },
        name = {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction = 0.8f)
                    .height(16.dp)
                    .shimmer()
            )
        },
        action = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
                    .shimmer()
            )
        },
        modifier = modifier
    )
}

@Preview
@Composable
private fun IngredientItemLoaderPreview() {
    LazyPizzaTheme {
        ToppingItemLoader()
    }
}