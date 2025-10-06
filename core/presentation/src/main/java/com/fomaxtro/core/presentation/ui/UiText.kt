package com.fomaxtro.core.presentation.ui

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.res.stringResource

@Stable
sealed interface UiText {
    @Stable
    data class StringResource(@field:StringRes val resId: Int) : UiText

    @Composable
    fun asString(): String {
        return when (this) {
            is StringResource -> stringResource(resId)
        }
    }

    fun asString(context: Context): String {
        return when (this) {
            is StringResource -> context.getString(resId)
        }
    }
}