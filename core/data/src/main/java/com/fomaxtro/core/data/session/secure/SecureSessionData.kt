package com.fomaxtro.core.data.session.secure

import com.fomaxtro.core.data.session.model.TokenPairSession
import kotlinx.serialization.Serializable

@Serializable
data class SecureSessionData(
    val tokenPair: TokenPairSession? = null
)
