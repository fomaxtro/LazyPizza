package com.fomaxtro.core.domain.model

typealias ProductId = Long

data class Product(
    val id: ProductId,
    val name: String,
    val description: String?,
    val price: Double,
    val imageUrl: String,
    val category: ProductCategory
)
