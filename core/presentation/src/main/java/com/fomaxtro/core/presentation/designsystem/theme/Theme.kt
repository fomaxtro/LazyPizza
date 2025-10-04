package com.fomaxtro.core.presentation.designsystem.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    background = BG,
    surface = SurfaceHigher,
    outline = Outline
)

val ColorScheme.textPrimary: Color
    get() = TextPrimary

val ColorScheme.textSecondary: Color
    get() = TextSecondary

val ColorScheme.textSecondary8: Color
    get() = TextSecondary8

val ColorScheme.textOnPrimary: Color
    get() = TextOnPrimary

val ColorScheme.surfaceHighest: Color
    get() = SurfaceHighest

val ColorScheme.outline50: Color
    get() = Outline50

val ColorScheme.primary8: Color
    get() = Primary8

val ColorScheme.primaryGradient: Brush
    get() = PrimaryGradient


@Composable
fun LazyPizzaTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}