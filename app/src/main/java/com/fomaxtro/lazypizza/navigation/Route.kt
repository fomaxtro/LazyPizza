package com.fomaxtro.lazypizza.navigation

import androidx.navigation3.runtime.NavKey
import com.fomaxtro.lazypizza.navigation.home.HomeRoute
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route : NavKey {
    @Serializable
    data class Home(
        val route: HomeRoute = HomeRoute.Menu
    ) : Route

    @Serializable
    data class ProductDetails(val productId: Long) : Route
}