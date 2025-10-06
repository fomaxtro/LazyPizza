package com.fomaxtro.core.presentation.designsystem.text_field

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fomaxtro.core.presentation.designsystem.theme.AppIcons
import com.fomaxtro.core.presentation.designsystem.theme.LazyPizzaTheme
import com.fomaxtro.core.presentation.designsystem.theme.textSecondary

@Composable
fun LazyPizzaOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null
) {
    val shape = CircleShape

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .dropShadow(
                shape = shape,
                shadow = Shadow(
                    radius = 16.dp,
                    color = Color(0xFF03131F).copy(alpha = 0.04f)
                )
            ),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.textSecondary,
            focusedPlaceholderColor = MaterialTheme.colorScheme.textSecondary
        ),
        shape = shape,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        textStyle = MaterialTheme.typography.bodyLarge
    )
}

@Preview(showBackground = true)
@Composable
private fun LazyPizzaOutlinedTextFieldPreview() {
    LazyPizzaTheme {
        LazyPizzaOutlinedTextField(
            value = "",
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            placeholder = {
                Text("Placeholder...")
            }
        )
    }
}