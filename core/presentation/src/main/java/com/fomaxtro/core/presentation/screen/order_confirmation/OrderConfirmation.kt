package com.fomaxtro.core.presentation.screen.order_confirmation

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fomaxtro.core.presentation.R
import com.fomaxtro.core.presentation.component.TopBarSheetSurface
import com.fomaxtro.core.presentation.designsystem.button.LazyPizzaTextButton
import com.fomaxtro.core.presentation.designsystem.theme.LazyPizzaTheme
import com.fomaxtro.core.presentation.designsystem.theme.body3Regular
import com.fomaxtro.core.presentation.designsystem.theme.label2Medium
import com.fomaxtro.core.presentation.designsystem.theme.label2Semibold
import com.fomaxtro.core.presentation.designsystem.theme.textSecondary
import com.fomaxtro.core.presentation.designsystem.theme.title1Medium
import com.fomaxtro.core.presentation.designsystem.top_bar.LazyPizzaCenteredAlignedTopAppBar
import com.fomaxtro.core.presentation.ui.Formatters
import com.fomaxtro.core.presentation.ui.ScreenType
import com.fomaxtro.core.presentation.ui.currentScreenType
import java.time.Instant
import java.time.ZoneId

@Composable
fun OrderConfirmation(
    onNavigateBack: () -> Unit,
    onNavigateToMenu: () -> Unit,
    orderId: Long,
    pickupTimeUtc: Long
) {
    val screenType = currentScreenType()

    Scaffold(
        topBar = {
            TopBarSheetSurface {
                LazyPizzaCenteredAlignedTopAppBar(
                    title = stringResource(R.string.order_checkout),
                    onNavigateBackClick = onNavigateBack,
                    containerColor = MaterialTheme.colorScheme.surface
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(top = 120.dp)
                .padding(horizontal = 16.dp)
                .then(
                    if (screenType == ScreenType.WIDE_SCREEN) {
                        Modifier.width(400.dp)
                    } else Modifier
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.order_placed),
                style = MaterialTheme.typography.title1Medium
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = stringResource(R.string.order_placed_subtitle),
                style = MaterialTheme.typography.body3Regular,
                color = MaterialTheme.colorScheme.textSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        color = MaterialTheme.colorScheme.outline,
                        width = 1.dp,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.order_number).uppercase() + ":",
                        style = MaterialTheme.typography.label2Medium,
                        color = MaterialTheme.colorScheme.textSecondary
                    )

                    Text(
                        text = "#$orderId",
                        style = MaterialTheme.typography.label2Semibold
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.pickup_time).uppercase() + ":",
                        style = MaterialTheme.typography.label2Medium,
                        color = MaterialTheme.colorScheme.textSecondary
                    )

                    val formattedPickupTime = Instant.ofEpochMilli(pickupTimeUtc)
                        .atZone(ZoneId.systemDefault())
                        .format(Formatters.pickupTimeFormatter)

                    Text(
                        text = formattedPickupTime,
                        style = MaterialTheme.typography.label2Semibold
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            LazyPizzaTextButton(
                onClick = onNavigateToMenu,
                text = stringResource(R.string.back_to_menu)
            )
        }
    }
}

@Preview
@Composable
private fun OrderConfirmationPreview() {
    LazyPizzaTheme {
        OrderConfirmation(
            orderId = 12345,
            pickupTimeUtc = Instant.now().toEpochMilli(),
            onNavigateBack = {},
            onNavigateToMenu = {}
        )
    }
}