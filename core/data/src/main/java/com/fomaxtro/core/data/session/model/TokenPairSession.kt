package com.fomaxtro.core.data.session.model

import kotlinx.serialization.Serializable

@Serializable
data class TokenPairSession(
    val accessToken: String,
    val refreshToken: String
)
