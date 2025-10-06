package com.fomaxtro.lazypizza

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.fomaxtro.core.presentation.designsystem.theme.LazyPizzaTheme
import com.fomaxtro.core.presentation.screen.home.HomeRoot

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()
        enableEdgeToEdge()

        setContent {
            LazyPizzaTheme {
                HomeRoot()
            }
        }
    }
}