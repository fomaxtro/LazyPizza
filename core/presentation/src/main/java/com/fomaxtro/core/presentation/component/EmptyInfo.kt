package com.fomaxtro.core.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fomaxtro.core.presentation.designsystem.button.LazyPizzaButton
import com.fomaxtro.core.presentation.designsystem.theme.LazyPizzaTheme
import com.fomaxtro.core.presentation.designsystem.theme.body3Regular
import com.fomaxtro.core.presentation.designsystem.theme.textSecondary
import com.fomaxtro.core.presentation.designsystem.theme.title1Medium
import com.fomaxtro.core.presentation.ui.ScreenType
import com.fomaxtro.core.presentation.ui.currentScreenType

@Composable
fun EmptyInfo(
    title: String,
    subtitle: String,
    action: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    val screenType = currentScreenType()
    val horizontalPadding = when (screenType) {
        ScreenType.MOBILE -> 16.dp
        ScreenType.WIDE_SCREEN -> 181.dp
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 120.dp)
            .padding(horizontal = horizontalPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.title1Medium,
            textAlign = TextAlign.Center
        )

        Text(
            text = subtitle,
            style = MaterialTheme.typography.body3Regular,
            color = MaterialTheme.colorScheme.textSecondary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        action()
    }
}

@Preview
@Composable
private fun EmptyInfoPreview() {
    LazyPizzaTheme {
        EmptyInfo(
            title = "Your cart is empty",
            subtitle = "Add some pizzas to get started",
            action = {
                LazyPizzaButton(
                    onClick = {},
                    text = "Add pizzas"
                )
            }
        )
    }
}