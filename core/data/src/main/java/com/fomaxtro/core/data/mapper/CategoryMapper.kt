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