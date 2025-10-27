package com.fomaxtro.core.presentation.util

import com.fomaxtro.core.domain.model.ProductCategory
import com.fomaxtro.core.presentation.R
import com.fomaxtro.core.presentation.model.ToppingSelectionUi
import com.fomaxtro.core.presentation.ui.UiText

fun ProductCategory.toDisplayName(): UiText {
    return when (this) {
        ProductCategory.PIZZA -> UiText.StringResource(R.string.pizza)
        ProductCategory.DRINKS -> UiText.StringResource(R.string.drinks)
        ProductCategory.SAUCES -> UiText.StringResource(R.string.sauces)
        ProductCategory.ICE_CREAM -> UiText.StringResource(R.string.ice_cream)
        ProductCategory.OTHER -> UiText.StringResource(R.string.other)
    }
}

fun ToppingSelectionUi.toDisplayText(): String {
    return "$quantity x ${topping.name}"
}