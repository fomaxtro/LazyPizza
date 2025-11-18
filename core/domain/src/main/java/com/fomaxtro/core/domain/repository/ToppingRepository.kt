package com.fomaxtro.core.domain.repository

import com.fomaxtro.core.domain.util.DataError
import com.fomaxtro.core.domain.model.Topping
import com.fomaxtro.core.domain.util.Result

interface ToppingRepository {
    suspend fun getAll(): Result<List<Topping>, DataError>
}