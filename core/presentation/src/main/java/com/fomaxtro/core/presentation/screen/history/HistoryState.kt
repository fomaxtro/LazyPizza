package com.fomaxtro.core.presentation.screen.history

import androidx.compose.runtime.Immutable
import com.fomaxtro.core.presentation.screen.history.model.OrderUi

@Immutable
data class HistoryState(
    val isAuthenticated: Boolean = false,
    val orders: List<OrderUi> = emptyList()
)