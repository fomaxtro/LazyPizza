package com.fomaxtro.core.data.repository

import com.fomaxtro.core.data.mapper.toOrder
import com.fomaxtro.core.data.mapper.toOrderRequest
import com.fomaxtro.core.data.remote.datasource.OrderRemoteDataSource
import com.fomaxtro.core.data.util.safeRemoteCall
import com.fomaxtro.core.domain.model.Order
import com.fomaxtro.core.domain.model.NewOrder
import com.fomaxtro.core.domain.repository.OrderRepository
import com.fomaxtro.core.domain.util.DataError
import com.fomaxtro.core.domain.util.Result
import com.fomaxtro.core.domain.util.map

class OrderRepositoryImpl(
    private val orderRemoteDataSource: OrderRemoteDataSource
) : OrderRepository {
    override suspend fun save(order: NewOrder): Result<Order, DataError> {
      return safeRemoteCall { orderRemoteDataSource.placeOrder(order.toOrderRequest()) }
          .map { it.toOrder() }
    }
}