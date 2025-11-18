package com.fomaxtro.core.presentation.verification

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class OtpCodeEventBus {
    private val eventChannel = Channel<String>()
    val events = eventChannel.receiveAsFlow()

    suspend fun sendOtpCode(otpCode: String) {
        eventChannel.send(otpCode)
    }
}