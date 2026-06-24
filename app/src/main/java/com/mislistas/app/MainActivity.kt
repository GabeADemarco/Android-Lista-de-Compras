package com.mislistas.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mislistas.app.ui.MainScreen
import com.mislistas.app.ui.theme.MisListasTheme
import com.mislistas.app.ui.theme.shouldUseDarkTheme
import com.mislistas.app.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: MainViewModel = viewModel()
            val themeMode by viewModel.themeMode.collectAsState()
            MisListasTheme(darkTheme = shouldUseDarkTheme(themeMode)) {
                MainScreen(viewModel = viewModel)
            }
        }
    }
}
