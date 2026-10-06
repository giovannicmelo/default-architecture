package com.giovannicampos.defaultarchitecture.feature.domain.usecase

import com.giovannicampos.defaultarchitecture.feature.domain.model.Feature
import com.giovannicampos.defaultarchitecture.feature.domain.repository.FeatureRepository

class GetFeaturesUseCase(
    private val repository: FeatureRepository,
) {

    suspend operator fun invoke(): List<Feature> = repository.getFeatures()
}
