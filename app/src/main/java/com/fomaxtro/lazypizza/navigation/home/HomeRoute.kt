package com.fomaxtro.lazypizza.navigation.home

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface HomeRoute : NavKey {
    @Serializable
    data object Menu : HomeRoute

    @Serializable
    data object Cart : HomeRoute

    @Serializable
    data object History : HomeRoute
}