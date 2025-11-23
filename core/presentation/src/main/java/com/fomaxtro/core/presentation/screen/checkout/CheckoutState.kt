package com.fomaxtro.core.presentation.screen.checkout

import androidx.compose.runtime.Immutable
import com.fomaxtro.core.presentation.model.CartItemUi
import com.fomaxtro.core.presentation.model.ProductUi
import com.fomaxtro.core.presentation.screen.checkout.model.PickupOption
import com.fomaxtro.core.presentation.util.Resource
import java.time.Instant
import java.time.temporal.ChronoUnit

@Immutable
data class CheckoutState(
    val pickupOption: PickupOption = PickupOption.EARLIEST,
    val pickupTime: Instant = Instant.now().plus(15, ChronoUnit.MINUTES),
    val cartItems: List<CartItemUi> = emptyList(),
    val productRecommendations: Resource<List<ProductUi>> = Resource.Loading,
    val comments: String = ""
) {
    val totalPrice: Double = cartItems.sumOf { it.totalPrice }
}
