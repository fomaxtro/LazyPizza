package com.fomaxtro.core.presentation.di

import com.fomaxtro.core.presentation.verification.OtpCodeEventBus
import com.fomaxtro.core.presentation.screen.cart.CartViewModel
import com.fomaxtro.core.presentation.screen.checkout.CheckoutViewModel
import com.fomaxtro.core.presentation.screen.history.HistoryViewModel
import com.fomaxtro.core.presentation.screen.home.HomeViewModel
import com.fomaxtro.core.presentation.screen.login.LoginViewModel
import com.fomaxtro.core.presentation.screen.menu.MenuViewModel
import com.fomaxtro.core.presentation.screen.product_details.ProductDetailsViewModel
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.auth.api.phone.SmsRetrieverClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val presentationModule = module {
    viewModelOf(::MenuViewModel)
    viewModel<ProductDetailsViewModel> { (productId: Long) ->
        ProductDetailsViewModel(
            productId = productId,
            productRepository = get(),
            toppingRepository = get(),
            cartUseCases = get()
        )
    }
    viewModelOf(::HomeViewModel)
    viewModelOf(::CartViewModel)
    viewModelOf(::HistoryViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::CheckoutViewModel)

    singleOf(::OtpCodeEventBus)
    single<SmsRetrieverClient> { SmsRetriever.getClient(androidContext()) }
}