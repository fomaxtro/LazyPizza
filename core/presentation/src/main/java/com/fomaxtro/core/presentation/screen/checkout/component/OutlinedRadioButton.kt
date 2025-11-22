package com.fomaxtro.core.presentation.screen.checkout.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fomaxtro.core.presentation.designsystem.theme.LazyPizzaTheme
import com.fomaxtro.core.presentation.designsystem.theme.body3Medium
import com.fomaxtro.core.presentation.designsystem.theme.textPrimary
import com.fomaxtro.core.presentation.designsystem.theme.textSecondary

@Composable
fun OutlinedRadioButton(
    onClick: () -> Unit,
    selected: Boolean,
    text: String,
    modifier: Modifier = Modifier,
    shape: Shape = CircleShape
) {
    Row(
        modifier = modifier
            .border(
                color = MaterialTheme.colorScheme.outline,
                width = 1.dp,
                shape = shape
            )
            .clip(shape)
            .clickable(onClick = onClick)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = null,
            colors = RadioButtonDefaults.colors(
                unselectedColor = MaterialTheme.colorScheme.textSecondary
            ),
            modifier = Modifier.padding(6.dp)
        )

        Text(
            text = text,
            style = MaterialTheme.typography.body3Medium,
            color = if (selected) {
                MaterialTheme.colorScheme.textPrimary
            } else {
                MaterialTheme.colorScheme.textSecondary
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun OutlinedRadioButtonPreview() {
    LazyPizzaTheme {
        OutlinedRadioButton(
            onClick = {},
            selected = true,
            text = "Earliest available time"
        )
    }
}