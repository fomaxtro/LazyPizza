package com.fomaxtro.core.presentation.screen.history.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.fomaxtro.core.presentation.R
import com.fomaxtro.core.presentation.designsystem.theme.LazyPizzaTheme
import com.fomaxtro.core.presentation.designsystem.theme.body4Regular
import com.fomaxtro.core.presentation.designsystem.theme.textPrimary
import com.fomaxtro.core.presentation.designsystem.theme.textSecondary
import com.fomaxtro.core.presentation.designsystem.theme.title3

@Composable
fun BaseHistoryListItem(
    title: @Composable () -> Unit,
    date: @Composable () -> Unit,
    products: @Composable () -> Unit,
    status: @Composable () -> Unit,
    price: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(12.dp)
) {
    Surface(
        modifier = modifier
            .height(IntrinsicSize.Max)
            .dropShadow(
                shape = shape,
                shadow = Shadow(
                    color = MaterialTheme.colorScheme.textPrimary.copy(alpha = 0.06f),
                    radius = 16.dp,
                    offset = DpOffset(0.dp, 4.dp)
                )
            ),
        color = MaterialTheme.colorScheme.surface,
        shape = shape,
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 12.dp
                ),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                CompositionLocalProvider(
                    LocalTextStyle provides MaterialTheme.typography.title3,
                    content = title
                )

                CompositionLocalProvider(
                    LocalTextStyle provides MaterialTheme.typography.body4Regular.copy(
                        color = MaterialTheme.colorScheme.textSecondary
                    ),
                    content = date
                )

                Spacer(modifier = Modifier.height(16.dp))

                CompositionLocalProvider(
                    LocalTextStyle provides MaterialTheme.typography.body4Regular,
                    content = products
                )
            }

            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.End
            ) {
                status()

                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = stringResource(R.string.total_amount) + ":",
                        style = MaterialTheme.typography.body4Regular,
                        color = MaterialTheme.colorScheme.textSecondary
                    )

                    CompositionLocalProvider(
                        LocalTextStyle provides MaterialTheme.typography.title3
                    ) {
                        price()
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun BaseHistoryListItemPreview() {
    LazyPizzaTheme {
        BaseHistoryListItem(
            modifier = Modifier.fillMaxWidth(),
            title = { Text("Order #1234") },
            date = { Text("September 25, 12:15") },
            products = {
                Text(
                    text = """
                        1 x Margherita
                        2 x Pepsi
                        2 x Cookies Ice Cream
                    """.trimIndent()
                )
            },
            status = {
                InfoLabel(
                    text = "In progress"
                )
            },
            price = {
                Text($$"$25.45")
            }
        )
    }
}