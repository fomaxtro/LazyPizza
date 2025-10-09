package com.fomaxtro.lazypizza.navigation

import androidx.navigation3.runtime.NavKey
import com.fomaxtro.core.domain.model.ProductId
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route : NavKey {
    @Serializable
    data object Home : Route

    @Serializable
    data class ProductDetails(val id: ProductId) : Route
}