package com.fomaxtro.core.domain.repository

import com.fomaxtro.core.domain.util.DataError
import com.fomaxtro.core.domain.model.Product
import com.fomaxtro.core.domain.model.ProductCategory
import com.fomaxtro.core.domain.util.Result

interface ProductRepository {
    suspend fun getAll(): Result<List<Product>, DataError>
    suspend fun findById(id: Long): Result<Product, DataError>
    suspend fun getAllByCategories(categories: Set<ProductCategory>): Result<List<Product>, DataError>
}