package com.fomaxtro.core.presentation.designsystem.button

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.fomaxtro.core.presentation.designsystem.theme.LazyPizzaTheme
import com.fomaxtro.core.presentation.designsystem.theme.PrimaryGradientStart
import com.fomaxtro.core.presentation.designsystem.theme.primaryGradient
import com.fomaxtro.core.presentation.designsystem.theme.textPrimary
import com.fomaxtro.core.presentation.designsystem.theme.title3

@Composable
fun LazyPizzaButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
) {
    val buttonHeight = ButtonDefaults.MinHeight - with(ButtonDefaults.ContentPadding) {
        calculateTopPadding() + calculateBottomPadding()
    }

    Button(
        onClick = {
            if (!loading) {
                onClick()
            }
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            disabledContentColor = MaterialTheme.colorScheme.textPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.textPrimary.copy(alpha = 0.08f)
        ),
        modifier = modifier
            .then(
                if (enabled) {
                    Modifier
                        .background(
                            brush = MaterialTheme.colorScheme.primaryGradient,
                            shape = ButtonDefaults.shape
                        )
                        .dropShadow(
                            shape = ButtonDefaults.shape,
                            shadow = Shadow(
                                color = PrimaryGradientStart.copy(alpha = 0.25f),
                                radius = 6.dp,
                                offset = DpOffset(0.dp, 4.dp)
                            )
                        )
                } else Modifier
            ),
        enabled = enabled
    ) {
        AnimatedContent(loading) { loading ->
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(buttonHeight),
                    color = LocalContentColor.current
                )
            } else {
                Text(
                    text = text,
                    style = MaterialTheme.typography.title3
                )
            }
        }
    }
}

@Preview
@Composable
private fun LazyPizzaPrimaryButtonPreview() {
    LazyPizzaTheme {
        LazyPizzaButton(
            onClick = {},
            text = "Primary Button",
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            enabled = true,
            loading = true
        )
    }
}