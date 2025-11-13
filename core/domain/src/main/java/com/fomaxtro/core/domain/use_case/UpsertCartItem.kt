package com.fomaxtro.core.domain.use_case

import com.fomaxtro.core.domain.model.CartItem
import com.fomaxtro.core.domain.repository.AuthRepository
import com.fomaxtro.core.domain.repository.AuthenticatedCartRepository
import com.fomaxtro.core.domain.repository.GuestCartRepository
import kotlinx.coroutines.flow.first

class UpsertCartItem(
    private val authRepository: AuthRepository,
    private val guestCartRepository: GuestCartRepository,
    private val authenticatedCartRepository: AuthenticatedCartRepository
) {
    suspend operator fun invoke(item: CartItem) {
        val isAuthenticated = authRepository.isAuthenticated().first()

        if (isAuthenticated) {
            authenticatedCartRepository.upsertCartItem(item)
        } else {
            guestCartRepository.upsertCartItem(item)
        }
    }
}