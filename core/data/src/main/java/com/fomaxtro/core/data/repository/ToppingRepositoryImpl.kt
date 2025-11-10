package com.fomaxtro.core.data.repository

import com.fomaxtro.core.data.mapper.toTopping
import com.fomaxtro.core.data.remote.datasource.ToppingRemoteDataSource
import com.fomaxtro.core.data.util.safeRemoteCall
import com.fomaxtro.core.domain.util.DataError
import com.fomaxtro.core.domain.model.Topping
import com.fomaxtro.core.domain.repository.ToppingRepository
import com.fomaxtro.core.domain.util.Result
import com.fomaxtro.core.domain.util.map

class ToppingRepositoryImpl(
    private val toppingRemoteDataSource: ToppingRemoteDataSource
) : ToppingRepository {
    override suspend fun getAll(): Result<List<Topping>, DataError> {
        return safeRemoteCall { toppingRemoteDataSource.fetchAll() }
            .map { topping -> topping.map { it.toTopping() } }
    }
}