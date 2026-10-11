package com.giovannicampos.defaultarchitecture.feature.sample.presentation.ui.effect

sealed interface SampleUiEffect {

    data class NavigateToSampleDetail(val sampleId: String) : SampleUiEffect

    data object ShowLoadError : SampleUiEffect
}
