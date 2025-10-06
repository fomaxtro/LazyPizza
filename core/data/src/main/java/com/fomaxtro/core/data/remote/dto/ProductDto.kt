package com.fomaxtro.core.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ProductDto(
    val id: Long,
    val name: String,
    val description: String?,
    val price: Double,
    val imageUrl: String,
    val category: CategoryDto
)
