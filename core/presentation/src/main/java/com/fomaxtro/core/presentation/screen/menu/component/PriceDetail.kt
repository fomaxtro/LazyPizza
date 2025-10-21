package com.fomaxtro.core.presentation.screen.menu.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.fomaxtro.core.presentation.designsystem.theme.LazyPizzaTheme
import com.fomaxtro.core.presentation.designsystem.theme.body4Regular
import com.fomaxtro.core.presentation.designsystem.theme.textSecondary
import com.fomaxtro.core.presentation.designsystem.theme.title1SemiBold
import com.fomaxtro.core.presentation.ui.Formatters

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
            text = Formatters.formatCurrency(price * quantity),
            style = MaterialTheme.typography.title1SemiBold
        )

        Text(
            text = "$quantity x ${Formatters.formatCurrency(price)}",
            style = MaterialTheme.typography.body4Regular,
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