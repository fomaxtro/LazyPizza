package com.fomaxtro.core.presentation.ui

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.window.core.layout.WindowSizeClass

enum class ScreenType {
    MOBILE,
    WIDE_SCREEN
}

@Composable
fun rememberScreenType(): ScreenType {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass

    return if (windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)) {
        ScreenType.WIDE_SCREEN
    } else {
        ScreenType.MOBILE
    }
}