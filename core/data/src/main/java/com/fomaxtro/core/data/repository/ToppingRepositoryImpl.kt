package com.fomaxtro.core.data.repository

import com.fomaxtro.core.data.mapper.toDataError
import com.fomaxtro.core.data.mapper.toTopping
import com.fomaxtro.core.data.remote.RemoteDataSource
import com.fomaxtro.core.data.util.safeRemoteCall
import com.fomaxtro.core.domain.error.DataError
import com.fomaxtro.core.domain.model.Topping
import com.fomaxtro.core.domain.repository.ToppingRepository
import com.fomaxtro.core.domain.util.Result
import com.fomaxtro.core.domain.util.map
import com.fomaxtro.core.domain.util.mapError

class ToppingRepositoryImpl(
    private val remoteDataSource: RemoteDataSource
) : ToppingRepository {
    override suspend fun getAll(): Result<List<Topping>, DataError> {
        return safeRemoteCall { remoteDataSource.fetchAllToppings() }
            .map { topping -> topping.map { it.toTopping() } }
            .mapError { it.toDataError() }
    }
}