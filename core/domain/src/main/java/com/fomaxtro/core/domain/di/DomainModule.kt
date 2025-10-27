package com.fomaxtro.core.domain.di

import com.fomaxtro.core.domain.use_case.ObserveCartItems
import com.fomaxtro.core.domain.use_case.ObserveProductsWithCartItems
import com.fomaxtro.core.domain.use_case.UpdateCartItemQuantity
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val domainModule = module {
    singleOf(::UpdateCartItemQuantity)
    singleOf(::ObserveProductsWithCartItems)
    singleOf(::ObserveCartItems)
}