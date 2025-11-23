package com.fomaxtro.core.domain.di

import com.fomaxtro.core.domain.use_case.CountCartItems
import com.fomaxtro.core.domain.use_case.GetCartItemsLocal
import com.fomaxtro.core.domain.use_case.GetProductRecommendations
import com.fomaxtro.core.domain.use_case.Login
import com.fomaxtro.core.domain.use_case.Logout
import com.fomaxtro.core.domain.use_case.ObserveCartItems
import com.fomaxtro.core.domain.use_case.ObserveProductsWithCartItems
import com.fomaxtro.core.domain.use_case.RemoveCartItem
import com.fomaxtro.core.domain.use_case.UpdateCartItemQuantity
import com.fomaxtro.core.domain.use_case.UpsertCartItem
import com.fomaxtro.core.domain.validation.OtpValidator
import com.fomaxtro.core.domain.validation.PhoneNumberValidator
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val domainModule = module {
    factoryOf(::UpdateCartItemQuantity)
    factoryOf(::ObserveProductsWithCartItems)
    factoryOf(::ObserveCartItems)
    factoryOf(::PhoneNumberValidator)
    factoryOf(::OtpValidator)
    factoryOf(::CountCartItems)
    factoryOf(::GetCartItemsLocal)
    factoryOf(::RemoveCartItem)
    factoryOf(::UpsertCartItem)
    factoryOf(::Login)
    factoryOf(::Logout)
    factoryOf(::GetProductRecommendations)
}