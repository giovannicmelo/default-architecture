package com.giovannicampos.defaultarchitecture.feature.presentation.ui.effect

sealed interface FeatureUiEffect {

    data class NavigateToFeatureDetail(val featureId: String) : FeatureUiEffect

    data object ShowLoadError : FeatureUiEffect
}
