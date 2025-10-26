package com.fomaxtro.core.data.mapper

import com.fomaxtro.core.data.remote.dto.CategoryDto
import com.fomaxtro.core.domain.model.ProductCategory

fun CategoryDto.toProductCategory(): ProductCategory {
    return when (id) {
        1L -> ProductCategory.PIZZA
        2L -> ProductCategory.DRINKS
        3L -> ProductCategory.SAUCES
        4L -> ProductCategory.ICE_CREAM
        else -> ProductCategory.OTHER
    }
}

fun ProductCategory.toCategoryId(): Long {
    return when (this) {
        ProductCategory.PIZZA -> 1L
        ProductCategory.DRINKS -> 2L
        ProductCategory.SAUCES -> 3L
        ProductCategory.ICE_CREAM -> 4L
        ProductCategory.OTHER -> -1L
    }
}