package com.fomaxtro.core.domain.use_case

import com.fomaxtro.core.domain.repository.AuthRepository
import com.fomaxtro.core.domain.repository.AuthenticatedCartRepository
import com.fomaxtro.core.domain.repository.GuestCartRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest

class CountCartItems(
    private val authRepository: AuthRepository,
    private val guestCartRepository: GuestCartRepository,
    private val authenticatedCartRepository: AuthenticatedCartRepository
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<Int> {
        return authRepository.isAuthenticated()
            .flatMapLatest { isAuthenticated ->
                if (isAuthenticated) {
                    authenticatedCartRepository.countCartItems()
                } else {
                    guestCartRepository.countCartItems()
                }
            }
    }
}