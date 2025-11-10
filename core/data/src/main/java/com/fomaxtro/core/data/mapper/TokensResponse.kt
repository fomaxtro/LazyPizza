package com.fomaxtro.core.data.mapper

import com.fomaxtro.core.data.remote.dto.TokensResponse
import com.fomaxtro.core.data.session.model.TokenPairSession

fun TokensResponse.toTokenPairSession() = TokenPairSession(
    accessToken = accessToken,
    refreshToken = refreshToken
)