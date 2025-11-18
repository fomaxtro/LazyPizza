package com.fomaxtro.core.presentation.designsystem.text_field

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.fomaxtro.core.presentation.designsystem.theme.surfaceHighest
import com.fomaxtro.core.presentation.designsystem.theme.textSecondary

object LazyPizzaOutlinedFormTextFieldDefaults {
    @Composable
    fun colors(
        containerColor: Color = MaterialTheme.colorScheme.surfaceHighest,
        placeholderColor: Color = MaterialTheme.colorScheme.textSecondary
    ): TextFieldColors {
        return OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = containerColor,
            unfocusedPlaceholderColor = placeholderColor,
            errorContainerColor = containerColor,
            errorPlaceholderColor = placeholderColor
        )
    }
}