package com.fomaxtro.core.domain.di

import com.fomaxtro.core.domain.use_case.AddCartItem
import com.fomaxtro.core.domain.use_case.CartUseCases
import com.fomaxtro.core.domain.use_case.ChangeCartItemQuantity
import com.fomaxtro.core.domain.use_case.CountCartItems
import com.fomaxtro.core.domain.use_case.GetCartItemsLocal
import com.fomaxtro.core.domain.use_case.ObserveProductRecommendations
import com.fomaxtro.core.domain.use_case.Login
import com.fomaxtro.core.domain.use_case.Logout
import com.fomaxtro.core.domain.use_case.ObserveCartItems
import com.fomaxtro.core.domain.use_case.ObserveProductsWithCartItems
import com.fomaxtro.core.domain.use_case.PlaceOrder
import com.fomaxtro.core.domain.use_case.RemoveCartItem
import com.fomaxtro.core.domain.use_case.UpdateCartItemQuantity
import com.fomaxtro.core.domain.validation.OtpValidator
import com.fomaxtro.core.domain.validation.PhoneNumberValidator
import com.fomaxtro.core.domain.validation.PickupTimeValidator
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val domainModule = module {
    factory<CartUseCases> {
        val addCartItem = AddCartItem(
            authRepository = get(),
            guestCartRepository = get(),
            authenticatedCartRepository = get()
        )
        val removeCartItem = RemoveCartItem(
            authRepository = get(),
            guestCartRepository = get(),
            authenticatedCartRepository = get()
        )
        val updateCartItemQuantity = UpdateCartItemQuantity(
            authRepository = get(),
            guestCartRepository = get(),
            authenticatedCartRepository = get()
        )
        val changeCartItemQuantity = ChangeCartItemQuantity(
            updateCartItemQuantity = updateCartItemQuantity,
            removeCartItem = removeCartItem
        )

        CartUseCases(
            addCartItem = addCartItem,
            removeCartItem = removeCartItem,
            changeCartItemQuantity = changeCartItemQuantity
        )
    }
    factoryOf(::ObserveProductsWithCartItems)
    factoryOf(::ObserveCartItems)
    factoryOf(::PhoneNumberValidator)
    factoryOf(::OtpValidator)
    factoryOf(::CountCartItems)
    factoryOf(::GetCartItemsLocal)
    factoryOf(::Login)
    factoryOf(::Logout)
    factoryOf(::ObserveProductRecommendations)
    factoryOf(::PickupTimeValidator)
    factoryOf(::PlaceOrder)
}