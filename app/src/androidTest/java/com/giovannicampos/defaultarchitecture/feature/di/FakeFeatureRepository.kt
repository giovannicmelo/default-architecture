package com.giovannicampos.defaultarchitecture.feature.di

import com.giovannicampos.defaultarchitecture.feature.domain.model.Feature
import com.giovannicampos.defaultarchitecture.feature.domain.repository.FeatureRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeFeatureRepository @Inject constructor() : FeatureRepository {

    var features: List<Feature> = emptyList()
    var error: Throwable? = null

    override suspend fun getFeatures(): List<Feature> = error?.let { throw it } ?: features
}
