package com.giovannicampos.defaultarchitecture

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.giovannicampos.defaultarchitecture.core.ui.theme.DefaultArchitectureTheme
import com.giovannicampos.defaultarchitecture.feature.presentation.ui.navigation.FeatureListRoute
import com.giovannicampos.defaultarchitecture.feature.presentation.ui.navigation.featureListScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DefaultArchitectureTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = FeatureListRoute) {
                    featureListScreen(
                        // Ainda não existe tela de detalhe para navegar.
                        onNavigateToFeatureDetail = {},
                    )
                }
            }
        }
    }
}
