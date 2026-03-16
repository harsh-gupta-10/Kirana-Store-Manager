package com.kiranstore.manager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.kiranstore.manager.ui.navigation.AppNavGraph
import com.kiranstore.manager.ui.theme.BackgroundGrey
import com.kiranstore.manager.ui.theme.KiranTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KiranTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = BackgroundGrey
                ) {
                    AppNavGraph()
                }
            }
        }
    }
}
