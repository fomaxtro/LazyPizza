package com.fomaxtro.core.presentation.screen.product_details.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fomaxtro.core.presentation.designsystem.theme.LazyPizzaTheme
import com.fomaxtro.core.presentation.designsystem.theme.textSecondary

@Composable
fun BaseToppingItem(
    image: @Composable () -> Unit,
    name: @Composable () -> Unit,
    action: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    selected: Boolean = false
) {
    Surface(
        modifier = modifier.height(142.dp),
        shape = ToppingItemDefaults.shape,
        border = BorderStroke(
            color = if (selected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.outline
            },
            width = 1.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                image()
            }

            CompositionLocalProvider(
                LocalTextStyle provides MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.textSecondary
                )
            ) {
                name()
            }

            Spacer(modifier = Modifier.height(8.dp))

            action()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BaseIngredientItemPreview() {
    LazyPizzaTheme {
        BaseToppingItem(
            image = {},
            name = {},
            action = {},
            modifier = Modifier.padding(8.dp),
            selected = true
        )
    }
}