package com.fomaxtro.core.presentation.screen.history.model

import androidx.compose.runtime.Immutable
import com.fomaxtro.core.domain.model.OrderStatus
import java.time.Instant

@Immutable
data class OrderUi(
    val id: Long,
    val totalPrice: Double,
    val status: OrderStatus,
    val createdAt: Instant
)
