package com.fomaxtro.core.presentation.screen.history.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fomaxtro.core.presentation.designsystem.modifier.shimmer
import com.fomaxtro.core.presentation.designsystem.theme.LazyPizzaTheme

@Composable
fun HistoryListItemLoader(
    modifier: Modifier = Modifier
) {
    BaseHistoryListItem(
        title = {
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .height(22.dp)
                    .shimmer()
            )
        },
        date = {
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .padding(top = 8.dp)
                    .height(8.dp)
                    .shimmer()
            )
        },
        products = {
            Box(
                modifier = Modifier
                    .width(121.dp)
                    .height(48.dp)
                    .shimmer()
            )
        },
        status = {
            Box(
                modifier = Modifier
                    .width(60.dp)
                    .height(20.dp)
                    .shimmer()
            )
        },
        price = {
            Box(
                modifier = Modifier
                    .width(77.dp)
                    .height(22.dp)
                    .shimmer()
            )
        },
        modifier = modifier
    )
}

@Preview
@Composable
private fun HistoryListItemLoaderPreview() {
    LazyPizzaTheme {
        HistoryListItemLoader(
            modifier = Modifier.fillMaxWidth()
        )
    }
}