package com.fomaxtro.core.presentation.screen.checkout

import com.fomaxtro.core.presentation.screen.checkout.model.PickupOption
import java.time.ZonedDateTime

sealed interface CheckoutAction {
    data class OnPickupTimeOptionSelected(val pickupOption: PickupOption) : CheckoutAction
    data class OnPickupDateTimeSelected(val dateTime: ZonedDateTime) : CheckoutAction
    data object OnPickupTimeDialogDismiss : CheckoutAction
    data class OnCartItemQuantityChange(val cartItemId: String, val quantity: Int) : CheckoutAction
    data class OnAddProductRecommendationClick(val productId: Long) : CheckoutAction
    data object OnNavigateBackClick : CheckoutAction
}