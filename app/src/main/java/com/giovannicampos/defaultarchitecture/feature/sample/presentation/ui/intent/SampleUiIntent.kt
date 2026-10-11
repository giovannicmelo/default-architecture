package com.giovannicampos.defaultarchitecture.feature.sample.presentation.ui.intent

sealed interface SampleUiIntent {

    data object LoadSamples : SampleUiIntent

    data class SampleClicked(val sampleId: String) : SampleUiIntent
}
