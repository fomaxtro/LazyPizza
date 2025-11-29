package com.fomaxtro.core.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class OrderToppingRequest(
    val id: Long,
    val quantity: Int
)
