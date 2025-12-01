package com.fomaxtro.core.domain.use_case

import com.fomaxtro.core.domain.repository.AuthRepository
import com.fomaxtro.core.domain.repository.AuthenticatedCartRepository
import com.fomaxtro.core.domain.repository.GuestCartRepository
import com.fomaxtro.core.domain.util.DataError
import com.fomaxtro.core.domain.util.EmptyResult
import com.fomaxtro.core.domain.util.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class Login(
    private val guestCartRepository: GuestCartRepository,
    private val authenticatedCartRepository: AuthenticatedCartRepository,
    private val authRepository: AuthRepository,
    private val applicationScope: CoroutineScope
) {
    suspend operator fun invoke(
        phoneNumber: String,
        code: String
    ): EmptyResult<DataError> {
        val result = authRepository.verifyOtp(
            phoneNumber = phoneNumber,
            code = code
        )

        if (result is Result.Success) {
            applicationScope.launch {
                val guestCart = guestCartRepository.getCartItemsLocal().first()

                if (guestCart.isNotEmpty()) {
                    authenticatedCartRepository.insertCartItemsLocal(guestCart)
                    guestCartRepository.clearCart()
                }
            }
        }

        return result
    }
}