package com.fomaxtro.core.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fomaxtro.core.presentation.designsystem.button.LazyPizzaButton
import com.fomaxtro.core.presentation.designsystem.theme.LazyPizzaTheme
import com.fomaxtro.core.presentation.designsystem.theme.fadeGradient

@Composable
fun GradientFadeBox(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(
        start = 16.dp,
        end = 16.dp,
        top = 36.dp,
        bottom = 16.dp
    ),
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.fadeGradient)
            .padding(contentPadding)
    ) {
        content()
    }
}

@Preview
@Composable
private fun GradientFadeBoxPreview() {
    LazyPizzaTheme {
        GradientFadeBox {
            LazyPizzaButton(
                onClick = {},
                text = "Button"
            )
        }
    }
}