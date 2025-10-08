package com.fomaxtro.core.domain.repository

import com.fomaxtro.core.domain.error.DataError
import com.fomaxtro.core.domain.model.Product
import com.fomaxtro.core.domain.util.Result

interface ProductRepository {
    suspend fun getAll(): Result<List<Product>, DataError>
    suspend fun findById(id: Long): Result<Product, DataError>
}