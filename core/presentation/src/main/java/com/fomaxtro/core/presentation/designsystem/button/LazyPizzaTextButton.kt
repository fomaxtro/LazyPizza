package com.fomaxtro.core.presentation.designsystem.button

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.fomaxtro.core.presentation.designsystem.theme.LazyPizzaTheme
import com.fomaxtro.core.presentation.designsystem.theme.title3

@Composable
fun LazyPizzaTextButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier
) {
    TextButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.title3
        )
    }
}

@Preview
@Composable
private fun LazyPizzaTextButtonPreview() {
    LazyPizzaTheme {
        LazyPizzaTextButton(
            onClick = {},
            text = "Text Button"
        )
    }
}