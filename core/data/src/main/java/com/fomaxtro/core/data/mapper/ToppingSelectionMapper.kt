package com.fomaxtro.core.data.mapper

import com.fomaxtro.core.data.session.model.ToppingSelectionSession
import com.fomaxtro.core.domain.model.ToppingSelection
import com.fomaxtro.core.domain.model.ToppingSelectionLocal

fun ToppingSelection.toToppingSelectionSession() = ToppingSelectionSession(
    id = topping.id,
    quantity = quantity
)

fun ToppingSelectionSession.toToppingSelectionLocal() = ToppingSelectionLocal(
    id = id,
    quantity = quantity
)
