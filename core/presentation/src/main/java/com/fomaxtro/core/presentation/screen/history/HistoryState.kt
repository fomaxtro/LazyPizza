package com.fomaxtro.core.presentation.screen.history

import androidx.compose.runtime.Immutable
import com.fomaxtro.core.presentation.screen.history.model.OrderUi
import com.fomaxtro.core.presentation.util.Resource

@Immutable
data class HistoryState(
    val isAuthenticated: Resource<Boolean> = Resource.Loading,
    val orders: List<OrderUi> = emptyList()
)