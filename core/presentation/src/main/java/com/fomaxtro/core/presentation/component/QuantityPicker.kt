package com.fomaxtro.core.presentation.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fomaxtro.core.presentation.R
import com.fomaxtro.core.presentation.designsystem.button.LazyPizzaIconButton
import com.fomaxtro.core.presentation.designsystem.theme.AppIcons
import com.fomaxtro.core.presentation.designsystem.theme.LazyPizzaTheme
import com.fomaxtro.core.presentation.designsystem.theme.textSecondary

@Composable
fun QuantityPicker(
    onQuantityChange: (Int) -> Unit,
    quantity: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        LazyPizzaIconButton(
            onClick = {
                onQuantityChange(quantity - 1)
            },
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = MaterialTheme.colorScheme.textSecondary
            )
        ) {
            Icon(
                imageVector = AppIcons.Filled.Minus,
                contentDescription = stringResource(R.string.minus)
            )
        }

        Text(
            text = quantity.toString(),
            modifier = Modifier.width(52.dp),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.MiddleEllipsis
        )

        LazyPizzaIconButton(
            onClick = {
                onQuantityChange(quantity + 1)
            },
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = MaterialTheme.colorScheme.textSecondary
            )
        ) {
            Icon(
                imageVector = AppIcons.Filled.Plus,
                contentDescription = stringResource(R.string.plus)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun QuantityPickerPreview() {
    LazyPizzaTheme {
        QuantityPicker(
            quantity = 1,
            onQuantityChange = {}
        )
    }
}