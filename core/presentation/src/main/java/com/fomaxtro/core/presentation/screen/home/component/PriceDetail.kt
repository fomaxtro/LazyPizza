package com.fomaxtro.core.presentation.screen.home.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.fomaxtro.core.presentation.designsystem.theme.LazyPizzaTheme
import com.fomaxtro.core.presentation.designsystem.theme.textSecondary
import com.fomaxtro.core.presentation.ui.Formatter

@Composable
fun PriceDetail(
    price: Double,
    quantity: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End
    ) {
        Text(
            text = Formatter.formatCurrency(price * quantity),
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = "$quantity x ${Formatter.formatCurrency(price)}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.textSecondary
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PriceDetailPreview() {
    LazyPizzaTheme {
        PriceDetail(
            price = 19.99,
            quantity = 2
        )
    }
}