package com.fomaxtro.core.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.fomaxtro.core.presentation.designsystem.card.LazyPizzaCardDefaults
import com.fomaxtro.core.presentation.designsystem.modifier.shimmer
import com.fomaxtro.core.presentation.designsystem.theme.LazyPizzaTheme
import com.fomaxtro.core.presentation.designsystem.theme.surfaceHighest
import com.fomaxtro.core.presentation.designsystem.theme.textPrimary

@Composable
fun BaseProductListItem(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    image: @Composable () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = modifier
            .heightIn(max = 128.dp)
            .dropShadow(
                shape = LazyPizzaCardDefaults.shape,
                shadow = Shadow(
                    color = MaterialTheme.colorScheme.textPrimary.copy(alpha = 0.06f),
                    radius = 16.dp,
                    offset = DpOffset(0.dp, 4.dp)
                )
            )
            .then(
                if (onClick != null) {
                    Modifier
                        .clip(LazyPizzaCardDefaults.shape)
                        .clickable(onClick = onClick)
                } else Modifier
            ),
        border = BorderStroke(
            color = MaterialTheme.colorScheme.surface,
            width = 1.dp
        ),
        shape = LazyPizzaCardDefaults.shape
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
                    .background(MaterialTheme.colorScheme.surfaceHighest),
                contentAlignment = Alignment.Center
            ) {
                image()
            }

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 12.dp
                    ),
                content = content
            )
        }
    }
}

@Preview
@Composable
private fun BaseProductListItemPreview() {
    LazyPizzaTheme {
        BaseProductListItem(
            image = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .shimmer()
                )
            }
        ) {

        }
    }
}