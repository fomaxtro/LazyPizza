package com.fomaxtro.core.domain.use_case

import com.fomaxtro.core.domain.repository.AuthRepository
import com.fomaxtro.core.domain.repository.AuthenticatedCartRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class Logout(
    private val authenticatedCartRepository: AuthenticatedCartRepository,
    private val authRepository: AuthRepository,
    private val applicationScope: CoroutineScope
) {
    suspend operator fun invoke() {
        authRepository.logout()

        applicationScope.launch {
            authenticatedCartRepository.clearCart()
        }
    }
}