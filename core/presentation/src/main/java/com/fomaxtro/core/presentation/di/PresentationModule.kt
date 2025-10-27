package com.fomaxtro.core.presentation.di

import com.fomaxtro.core.presentation.screen.cart.CartViewModel
import com.fomaxtro.core.presentation.screen.home.HomeViewModel
import com.fomaxtro.core.presentation.screen.menu.MenuViewModel
import com.fomaxtro.core.presentation.screen.product_details.ProductDetailsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val presentationModule = module {
    viewModelOf(::MenuViewModel)
    viewModel<ProductDetailsViewModel> { (productId: Long) ->
        ProductDetailsViewModel(
            productId = productId,
            productRepository = get(),
            toppingRepository = get()
        )
    }
    viewModelOf(::HomeViewModel)
    viewModelOf(::CartViewModel)
}