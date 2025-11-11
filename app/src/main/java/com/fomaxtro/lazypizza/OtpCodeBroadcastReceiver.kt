package com.fomaxtro.lazypizza

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.fomaxtro.core.presentation.verification.OtpCodeEventBus
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@Suppress("DEPRECATION")
class OtpCodeBroadcastReceiver : BroadcastReceiver(), KoinComponent {
    private val otpCodeEventBus: OtpCodeEventBus by inject()

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null) return

        if (SmsRetriever.SMS_RETRIEVED_ACTION == intent.action) {
            val status = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.extras?.getParcelable(SmsRetriever.EXTRA_STATUS, Status::class.java)
            } else {
                intent.extras?.getParcelable(SmsRetriever.EXTRA_STATUS)
            }

            when (status?.statusCode) {
                CommonStatusCodes.SUCCESS -> {
                    val message = intent.extras?.getString(SmsRetriever.EXTRA_SMS_MESSAGE)

                    message?.let {
                        extractOtpCode(it)?.let { code ->
                            CoroutineScope(Dispatchers.IO).launch {
                                otpCodeEventBus.sendOtpCode(code)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun extractOtpCode(message: String): String? {
        return Regex("\\d{6}").find(message)?.value
    }
}