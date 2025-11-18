package com.fomaxtro.core.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class SendOtpRequest(
    val phoneNumber: String
)
