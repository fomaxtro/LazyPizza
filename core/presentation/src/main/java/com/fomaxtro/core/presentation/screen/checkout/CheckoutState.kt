package com.fomaxtro.core.presentation.screen.checkout

import androidx.compose.runtime.Immutable
import com.fomaxtro.core.presentation.model.CartItemUi
import com.fomaxtro.core.presentation.model.ProductUi
import com.fomaxtro.core.presentation.screen.checkout.model.PickupOption
import com.fomaxtro.core.presentation.ui.Resource
import com.fomaxtro.core.presentation.ui.getOrDefault
import java.time.Instant
import java.time.temporal.ChronoUnit

@Immutable
data class CheckoutState(
    val pickupOption: PickupOption = PickupOption.EARLIEST,
    val pickupTime: Instant = Instant.now().plus(15, ChronoUnit.MINUTES),
    val isDateTimePickerDialogVisible: Boolean = false,
    val cartItems: Resource<List<CartItemUi>> = Resource.Loading,
    val productRecommendations: Resource<List<ProductUi>> = Resource.Loading,
    val comments: String = ""
) {
    val totalPrice: Double = cartItems.getOrDefault(emptyList())
        .sumOf { it.totalPrice }
}
