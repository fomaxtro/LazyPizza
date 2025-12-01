package com.fomaxtro.core.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.fomaxtro.core.presentation.designsystem.card.LazyPizzaCardDefaults
import com.fomaxtro.core.presentation.designsystem.theme.LazyPizzaTheme
import com.fomaxtro.core.presentation.designsystem.theme.surfaceHighest
import com.fomaxtro.core.presentation.designsystem.theme.textPrimary

@Composable
fun BaseProductRecommendationCard(
    image: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = modifier
            .border(
                color = MaterialTheme.colorScheme.surface,
                width = 1.dp,
                shape = CardDefaults.shape
            )
            .dropShadow(
                shape = LazyPizzaCardDefaults.shape,
                shadow = Shadow(
                    color = MaterialTheme.colorScheme.textPrimary.copy(alpha = 0.06f),
                    radius = 16.dp,
                    offset = DpOffset(0.dp, 4.dp)
                )
            )
            .width(IntrinsicSize.Max),
        shape = LazyPizzaCardDefaults.shape
    ) {
        Column(
            modifier = Modifier
        ) {
            Box(
                modifier = Modifier
                    .width(180.dp)
                    .height(120.dp)
                    .background(MaterialTheme.colorScheme.surfaceHighest),
                contentAlignment = Alignment.Center
            ) {
                image()
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                content = content
            )
        }
    }
}

@Preview
@Composable
private fun BaseProductRecommendationCardPreview() {
    LazyPizzaTheme {
        BaseProductRecommendationCard(
            image = {

            }
        ) {

        }
    }
}