package com.fomaxtro.lazypizza

import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fomaxtro.core.presentation.designsystem.theme.LazyPizzaTheme
import com.fomaxtro.lazypizza.app.AppViewModel
import com.fomaxtro.lazypizza.navigation.NavigationRoot
import com.google.android.gms.auth.api.phone.SmsRetriever
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class MainActivity : ComponentActivity() {
    private val otpCodeReceiver = OtpCodeBroadcastReceiver()
    private val viewModel: AppViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().setKeepOnScreenCondition {
            !viewModel.state.value.isAuthChecked
        }
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
            val state by viewModel.state.collectAsStateWithLifecycle()

            LazyPizzaTheme {
                if (state.isAuthChecked) {
                    NavigationRoot(
                        isAuthenticated = state.isAuthenticated
                    )
                }
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