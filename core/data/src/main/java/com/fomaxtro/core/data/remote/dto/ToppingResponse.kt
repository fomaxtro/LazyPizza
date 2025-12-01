package com.fomaxtro.core.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ToppingResponse(
    val id: Long,
    val name: String,
    val price: Double,
    val imageUrl: String
)
