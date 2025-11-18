package com.fomaxtro.lazypizza

import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.fomaxtro.core.presentation.designsystem.theme.LazyPizzaTheme
import com.fomaxtro.lazypizza.navigation.NavigationRoot
import com.google.android.gms.auth.api.phone.SmsRetriever
import timber.log.Timber

class MainActivity : ComponentActivity() {
    private val otpCodeReceiver = OtpCodeBroadcastReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()
        enableEdgeToEdge()

        ContextCompat.registerReceiver(
            this,
            otpCodeReceiver,
            IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION),
            null,
            null,
            ContextCompat.RECEIVER_EXPORTED
        )

        setContent {
            LazyPizzaTheme {
                NavigationRoot()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        try {
            unregisterReceiver(otpCodeReceiver)
        } catch (e: IllegalArgumentException) {
            Timber.tag("MainActivity").e(e, "unregisterReceiver failed")
        }
    }
}