package com.giovannicampos.defaultarchitecture.feature.sample.presentation.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.giovannicampos.defaultarchitecture.feature.sample.presentation.ui.screen.SampleRoute
import kotlinx.serialization.Serializable

@Serializable
data object SampleListRoute

fun NavGraphBuilder.sampleListScreen(
    onNavigateToSampleDetail: (sampleId: String) -> Unit,
) {
    composable<SampleListRoute> {
        SampleRoute(onNavigateToSampleDetail = onNavigateToSampleDetail)
    }
}
