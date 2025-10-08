package com.fomaxtro.core.data.repository

import com.fomaxtro.core.data.mapper.toDataError
import com.fomaxtro.core.data.mapper.toProduct
import com.fomaxtro.core.data.remote.RemoteDataSource
import com.fomaxtro.core.data.util.safeRemoteCall
import com.fomaxtro.core.domain.error.DataError
import com.fomaxtro.core.domain.model.Product
import com.fomaxtro.core.domain.repository.ProductRepository
import com.fomaxtro.core.domain.util.Result
import com.fomaxtro.core.domain.util.map
import com.fomaxtro.core.domain.util.mapError

class ProductRepositoryImpl(
    private val remoteDataSource: RemoteDataSource
) : ProductRepository {
    override suspend fun getAll(): Result<List<Product>, DataError> {
        return safeRemoteCall { remoteDataSource.fetchAllProducts() }
            .map { products -> products.map { it.toProduct() } }
            .mapError { it.toDataError() }
    }

    override suspend fun findById(id: Long): Result<Product, DataError> {
        return safeRemoteCall { remoteDataSource.findProductById(id) }
            .map { it.toProduct() }
            .mapError { it.toDataError() }
    }
}