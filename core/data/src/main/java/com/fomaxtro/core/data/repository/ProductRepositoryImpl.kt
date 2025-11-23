package com.fomaxtro.core.data.repository

import com.fomaxtro.core.data.mapper.toCategoryId
import com.fomaxtro.core.data.mapper.toProduct
import com.fomaxtro.core.data.remote.datasource.ProductRemoteDataSource
import com.fomaxtro.core.data.util.safeRemoteCall
import com.fomaxtro.core.domain.util.DataError
import com.fomaxtro.core.domain.model.Product
import com.fomaxtro.core.domain.model.ProductCategory
import com.fomaxtro.core.domain.repository.ProductRepository
import com.fomaxtro.core.domain.util.Result
import com.fomaxtro.core.domain.util.map

class ProductRepositoryImpl(
    private val productRemoteDataSource: ProductRemoteDataSource
) : ProductRepository {
    override suspend fun getAll(): Result<List<Product>, DataError> {
        return safeRemoteCall { productRemoteDataSource.fetchAll() }
            .map { products -> products.map { it.toProduct() } }
    }

    override suspend fun findById(id: Long): Result<Product, DataError> {
        return safeRemoteCall { productRemoteDataSource.findById(id) }
            .map { it.toProduct() }
    }

    override suspend fun getAllByCategories(categories: Set<ProductCategory>): Result<List<Product>, DataError> {
        val categoryIds = categories.map { it.toCategoryId() }

        return safeRemoteCall { productRemoteDataSource.fetchAllByCategories(categoryIds) }
            .map { products ->
                products.map { it.toProduct() }
            }
    }
}