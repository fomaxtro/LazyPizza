package com.fomaxtro.core.presentation.designsystem.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val TextPrimary = Color(0xFF03131F)
val TextSecondary = Color(0xFF627686)
val TextSecondary8 = TextSecondary.copy(alpha = 0.08f)
val TextOnPrimary = Color(0xFFFFFFFF)

val BG = Color(0xFFFAFBFC)
val SurfaceHigher = Color(0xFFFFFFFF)
val SurfaceHighest = Color(0xFFF0F3F6)

val Outline = Color(0xFFE6E7ED)
val Outline50 = Outline.copy(alpha = 0.5f)

val Overlay = Color(0xFF03131F).copy(alpha = 0.6f)

val PrimaryGradientStart = Color(0xFFF36B50)
val PrimaryGradientEnd = Color(0xFFF9966F)

val PrimaryGradient: Brush
    get() = Brush.horizontalGradient(
        colors = listOf(
            PrimaryGradientStart,
            PrimaryGradientEnd
        )
    )

val FadeGradient: Brush
    get() = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFFFFFFF).copy(alpha = 0f),
            Color(0xFFFFFFFF)
        )
    )

val Primary = Color(0xFFF36B50)
val Primary8 = Primary.copy(alpha = 0.08f)