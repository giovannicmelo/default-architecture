package com.giovannicampos.defaultarchitecture.feature.presentation.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.giovannicampos.defaultarchitecture.feature.presentation.ui.screen.FeatureRoute
import kotlinx.serialization.Serializable

@Serializable
data object FeatureListRoute

fun NavGraphBuilder.featureListScreen(
    onNavigateToFeatureDetail: (featureId: String) -> Unit,
) {
    composable<FeatureListRoute> {
        FeatureRoute(onNavigateToFeatureDetail = onNavigateToFeatureDetail)
    }
}
