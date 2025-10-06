package com.fomaxtro.core.presentation.screen.home.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fomaxtro.core.presentation.designsystem.modifier.shimmer
import com.fomaxtro.core.presentation.designsystem.theme.LazyPizzaTheme

@Composable
fun ProductListItemLoader(
    modifier: Modifier = Modifier
) {
    BaseProductListItem(
        modifier = modifier,
        image = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .shimmer()
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(fraction = 0.6f)
                .height(16.dp)
                .shimmer()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
                .shimmer()
        )

        Box(
            modifier = Modifier
                .width(64.dp)
                .weight(1f)
                .wrapContentHeight(align = Alignment.Bottom)
                .height(24.dp)
                .shimmer()
        )
    }
}

@Preview
@Composable
private fun ProductListItemLoaderPreview() {
    LazyPizzaTheme {
        ProductListItemLoader()
    }
}