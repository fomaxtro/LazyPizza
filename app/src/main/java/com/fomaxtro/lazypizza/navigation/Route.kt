package com.fomaxtro.lazypizza.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route : NavKey {
    @Serializable
    data class Home(val refreshKey: Long = System.currentTimeMillis()) : Route

    @Serializable
    data class ProductDetails(val productId: Long) : Route

    @Serializable
    data object Login : Route

    @Serializable
    data object Checkout : Route

    data class OrderConfirmation(
        val orderId: Long,
        val pickupTimeUtc: Long
    ) : Route
}