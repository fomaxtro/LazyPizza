package com.fomaxtro.lazypizza

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.fomaxtro.core.presentation.designsystem.theme.LazyPizzaTheme
import com.fomaxtro.lazypizza.app.AppViewModel
import com.fomaxtro.lazypizza.navigation.NavigationRoot
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private val viewModel by viewModel<AppViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().setKeepOnScreenCondition {
            !viewModel.state.value.isAuthChecked
        }
        enableEdgeToEdge()

        setContent {
            LazyPizzaTheme {
                NavigationRoot(
                    isAuthenticated = viewModel.state.value.isAuthenticated
                )
            }
        }
    }
}