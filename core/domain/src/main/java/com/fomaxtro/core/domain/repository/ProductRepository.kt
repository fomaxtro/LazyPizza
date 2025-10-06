package com.fomaxtro.core.domain.repository

import com.fomaxtro.core.domain.error.DataError
import com.fomaxtro.core.domain.model.Product
import com.fomaxtro.core.domain.util.Result

interface ProductRepository {
    suspend fun getAllProducts(): Result<List<Product>, DataError>
}