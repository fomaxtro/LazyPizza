package com.fomaxtro.core.data.remote.util

import com.fomaxtro.core.data.BuildConfig

fun createRoute(route: String): String {
    return when {
        route.startsWith("/") -> BuildConfig.API_URL + route
        route.startsWith("http") -> route
        else -> BuildConfig.API_URL + "/$route"
    }
}