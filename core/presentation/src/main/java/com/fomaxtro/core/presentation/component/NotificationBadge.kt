package com.fomaxtro.core.presentation.component

import androidx.annotation.IntRange
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Badge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.fomaxtro.core.presentation.designsystem.theme.LazyPizzaTheme
import com.fomaxtro.core.presentation.designsystem.theme.title4

@Composable
fun NotificationBadge(
    @IntRange(from = 1) value: Int,
    modifier: Modifier = Modifier
) {
    Badge(
        modifier = Modifier
            .padding(
                bottom = 6.dp,
                start = 6.dp
            )
            .dropShadow(
                shape = CircleShape,
                shadow = Shadow(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.25f),
                    radius = 6.dp,
                    offset = DpOffset(0.dp, 4.dp)
                )
            ),
        containerColor = MaterialTheme.colorScheme.primary
    ) {
        Text(
            text = value.toString(),
            style = MaterialTheme.typography.title4
        )
    }
}

@Preview
@Composable
private fun NotificationBadgePreview() {
    LazyPizzaTheme {
        NotificationBadge(
            value = 1
        )
    }
}