package com.fomaxtro.core.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fomaxtro.core.presentation.R
import com.fomaxtro.core.presentation.designsystem.theme.LazyPizzaTheme
import com.fomaxtro.core.presentation.designsystem.theme.primary8
import com.fomaxtro.core.presentation.designsystem.theme.textPrimary
import com.fomaxtro.core.presentation.designsystem.theme.textSecondary
import com.fomaxtro.core.presentation.designsystem.theme.title4

@Composable
fun NavigationButton(
    selected: Boolean,
    icon: @Composable () -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .width(96.dp)
            .height(66.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .then(
                    if (selected) {
                        Modifier.background(
                            color = MaterialTheme.colorScheme.primary8,
                            shape = CircleShape
                        )
                    } else Modifier
                )
                .padding(6.dp),
            contentAlignment = Alignment.Center
        ) {
            CompositionLocalProvider(
                LocalContentColor provides if (selected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.textSecondary
                }
            ) {
                icon()
            }
        }

        Text(
            text = label,
            style = MaterialTheme.typography.title4,
            color = if (selected) {
                MaterialTheme.colorScheme.textPrimary
            } else {
                MaterialTheme.colorScheme.textSecondary
            }
        )
    }
}

@Preview
@Composable
private fun NavigationButtonPreview() {
    LazyPizzaTheme {
        NavigationButton(
            selected = true,
            icon = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.menu),
                    contentDescription = null
                )
            },
            label = "Menu"
        )
    }
}