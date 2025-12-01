package com.fomaxtro.core.presentation.screen.checkout

import com.fomaxtro.core.presentation.ui.UiText

sealed interface CheckoutEvent {
    data class ShowSystemMessage(val message: UiText) : CheckoutEvent
    data class ShowMessage(val message: UiText) : CheckoutEvent
    data class NavigateToOrderConfirmation(
        val orderId: Long,
        val pickupTimeUtc: Long
    ) : CheckoutEvent
}