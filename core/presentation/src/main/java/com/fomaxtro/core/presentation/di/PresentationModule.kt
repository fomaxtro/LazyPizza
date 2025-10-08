package com.fomaxtro.core.presentation.di

import com.fomaxtro.core.domain.model.ProductId
import com.fomaxtro.core.presentation.screen.home.HomeViewModel
import com.fomaxtro.core.presentation.screen.product_details.ProductDetailsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val presentationModule = module {
    viewModelOf(::HomeViewModel)
    viewModel<ProductDetailsViewModel> { (id: ProductId) ->
        ProductDetailsViewModel(
            id = id,
            productRepository = get(),
            toppingRepository = get()
        )
    }
}