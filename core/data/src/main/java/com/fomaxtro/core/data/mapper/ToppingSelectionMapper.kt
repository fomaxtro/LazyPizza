package com.fomaxtro.core.data.mapper

import com.fomaxtro.core.data.database.entity.ToppingSelectionEntity
import com.fomaxtro.core.data.remote.dto.OrderToppingRequest
import com.fomaxtro.core.data.session.model.ToppingSelectionSession
import com.fomaxtro.core.domain.model.ToppingSelection
import com.fomaxtro.core.domain.model.ToppingSelectionLocal
import java.util.UUID

fun ToppingSelection.toToppingSelectionSession() = ToppingSelectionSession(
    id = topping.id,
    quantity = quantity
)

fun ToppingSelectionSession.toToppingSelectionLocal() = ToppingSelectionLocal(
    id = id,
    quantity = quantity
)

fun ToppingSelectionEntity.toToppingSelection() = ToppingSelectionLocal(
    id = toppingId,
    quantity = quantity
)

fun ToppingSelection.toToppingSelectionEntity(
    cartItemId: UUID
) = ToppingSelectionEntity(
    toppingId = topping.id,
    quantity = quantity,
    cartItemId = cartItemId.toString()
)

fun ToppingSelectionLocal.toToppingSelectionEntity(
    cartItemId: UUID
) = ToppingSelectionEntity(
    toppingId = id,
    quantity = quantity,
    cartItemId = cartItemId.toString()
)

fun ToppingSelection.toOrderToppingRequest() = OrderToppingRequest(
    id = topping.id,
    quantity = quantity
)