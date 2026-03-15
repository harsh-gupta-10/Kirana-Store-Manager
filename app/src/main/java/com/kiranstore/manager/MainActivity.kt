package com.kiranstore.manager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.kiranstore.manager.ui.navigation.AppNavHost
import com.kiranstore.manager.ui.navigation.Routes
import com.kiranstore.manager.ui.theme.KiranStoreTheme
import com.kiranstore.manager.ui.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // Install splash screen before super.onCreate
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            KiranStoreTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val authVm: AuthViewModel = hiltViewModel()
                    val authState by authVm.state.collectAsState()

                    // Keep splash visible while checking session
                    splashScreen.setKeepOnScreenCondition { authState.isLoading }

                    val navController = rememberNavController()

                    // Determine start destination
                    val startDestination = remember(authState.isLoggedIn) {
                        when {
                            authState.isLoggedIn && authState.isNewUser -> Routes.SHOP_SETUP
                            authState.isLoggedIn                        -> Routes.HOME
                            else                                        -> Routes.LOGIN
                        }
                    }

                    AppNavHost(
                        navController = navController,
                        startDestination = startDestination
                    )
                }
            }
        }
    }
}
