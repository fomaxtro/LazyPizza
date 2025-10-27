package com.fomaxtro.core.presentation.designsystem.button

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fomaxtro.core.presentation.R
import com.fomaxtro.core.presentation.designsystem.theme.AppIcons
import com.fomaxtro.core.presentation.designsystem.theme.LazyPizzaTheme
import com.fomaxtro.core.presentation.designsystem.theme.outline50

@Composable
fun LazyPizzaIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    enabled: Boolean = true,
    content: @Composable () -> Unit
) {
    IconButton(
        onClick = onClick,
        colors = IconButtonDefaults.iconButtonColors(
            contentColor = color
        ),
        modifier = modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline50,
                shape = RoundedCornerShape(8.dp)
            )
            .size(22.dp),
        enabled = enabled
    ) {
        Box(
            modifier = Modifier.size(14.dp)
        ) {
            content()
        }
    }
}

@Preview
@Composable
private fun LazyPizzaIconButtonPreview() {
    LazyPizzaTheme {
        LazyPizzaIconButton(
            onClick = {}
        ) {
            Icon(
                imageVector = AppIcons.Outlined.Trash,
                contentDescription = stringResource(R.string.delete)
            )
        }
    }
}