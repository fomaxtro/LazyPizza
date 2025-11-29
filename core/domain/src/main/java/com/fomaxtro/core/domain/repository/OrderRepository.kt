package com.fomaxtro.core.domain.repository

import com.fomaxtro.core.domain.model.Order
import com.fomaxtro.core.domain.model.NewOrder
import com.fomaxtro.core.domain.util.DataError
import com.fomaxtro.core.domain.util.Result

interface OrderRepository {
    suspend fun save(order: NewOrder): Result<Order, DataError>
}