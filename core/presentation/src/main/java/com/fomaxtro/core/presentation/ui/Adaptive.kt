package com.fomaxtro.core.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp

enum class ScreenType {
    MOBILE,
    WIDE_SCREEN
}

@Composable
fun rememberScreenType(): ScreenType {
    val windowInfo = LocalWindowInfo.current
    val targetWidthPx = with(LocalDensity.current) {
        840.dp.toPx()
    }

    return if (windowInfo.containerSize.width > targetWidthPx) {
        ScreenType.WIDE_SCREEN
    } else {
        ScreenType.MOBILE
    }
}