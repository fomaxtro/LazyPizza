package com.fomaxtro.core.presentation.screen.history.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.fomaxtro.core.domain.model.OrderStatus
import com.fomaxtro.core.presentation.R
import com.fomaxtro.core.presentation.designsystem.theme.LazyPizzaTheme
import com.fomaxtro.core.presentation.designsystem.theme.body4Regular
import com.fomaxtro.core.presentation.designsystem.theme.success
import com.fomaxtro.core.presentation.designsystem.theme.textSecondary
import com.fomaxtro.core.presentation.designsystem.theme.waring
import com.fomaxtro.core.presentation.screen.history.model.OrderProductUiFactory
import com.fomaxtro.core.presentation.screen.history.model.OrderUi
import com.fomaxtro.core.presentation.screen.history.model.OrderUiFactory
import com.fomaxtro.core.presentation.ui.Formatters
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun HistoryListItem(
    order: OrderUi,
    modifier: Modifier = Modifier
) {
    BaseHistoryListItem(
        title = {
            Text(stringResource(R.string.order, order.id))
        },
        date = {
            Text(
                text = order.pickupTime
                    .atZone(ZoneId.systemDefault())
                    .format(
                        DateTimeFormatter.ofPattern(
                            "MMMM dd, HH:mm",
                            Locale.getDefault()
                        )
                    )
            )
        },
        products = {
            val formattedProducts = order.products
                .joinToString("\n") {
                    "${it.quantity}x ${it.name}"
                }

            Text(
                text = formattedProducts,
                style = MaterialTheme.typography.body4Regular
            )
        },
        status = {
            when (order.status) {
                OrderStatus.COMPLETED -> {
                    InfoLabel(
                        text = stringResource(R.string.completed),
                        containerColor = MaterialTheme.colorScheme.success
                    )
                }
                OrderStatus.CANCELLED -> {
                    InfoLabel(
                        text = stringResource(R.string.cancelled),
                        containerColor = MaterialTheme.colorScheme.textSecondary
                    )
                }
                OrderStatus.IN_PROGRESS -> {
                    InfoLabel(
                        text = stringResource(R.string.in_progress_text),
                        containerColor = MaterialTheme.colorScheme.waring
                    )
                }
            }
        },
        price = {
            Text(
                text = Formatters.formatCurrency(order.totalPrice)
            )
        },
        modifier = modifier
    )
}

@Preview
@Composable
private fun HistoryListItemPreview() {
    LazyPizzaTheme {
        HistoryListItem(
            modifier = Modifier.fillMaxWidth(),
            order = OrderUiFactory.create(
                id = 1,
                products = (1..2).map {
                    OrderProductUiFactory.create(
                        id = it.toLong()
                    )
                },
            )
        )
    }
}