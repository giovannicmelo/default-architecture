package com.giovannicampos.defaultarchitecture.feature.presentation.ui.intent

sealed interface FeatureUiIntent {

    data object LoadFeatures : FeatureUiIntent

    data class FeatureClicked(val featureId: String) : FeatureUiIntent
}
