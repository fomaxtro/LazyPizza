package com.fomaxtro.core.data.mapper

import com.fomaxtro.core.data.session.model.ToppingSelectionSession
import com.fomaxtro.core.domain.model.ToppingSelection

fun ToppingSelection.toToppingSelectionSession() = ToppingSelectionSession(
    id = topping.id,
    quantity = quantity
)