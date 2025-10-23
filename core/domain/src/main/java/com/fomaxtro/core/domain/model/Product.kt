package com.fomaxtro.core.domain.model

data class Product(
    val id: Long,
    val name: String,
    val description: String?,
    val price: Double,
    val imageUrl: String,
    val category: ProductCategory
)
