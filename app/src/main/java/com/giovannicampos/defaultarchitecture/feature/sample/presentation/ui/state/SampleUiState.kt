package com.giovannicampos.defaultarchitecture.feature.sample.presentation.ui.state

import com.giovannicampos.defaultarchitecture.feature.sample.domain.model.Sample

data class SampleUiState(
    val isLoading: Boolean = false,
    val samples: List<Sample> = emptyList(),
)
