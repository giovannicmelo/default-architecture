package com.giovannicampos.defaultarchitecture.feature.presentation.ui.state

import com.giovannicampos.defaultarchitecture.feature.domain.model.Feature

data class FeatureUiState(
    val isLoading: Boolean = false,
    val features: List<Feature> = emptyList(),
)
