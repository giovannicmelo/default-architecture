package com.giovannicampos.defaultarchitecture.feature.domain.repository

import com.giovannicampos.defaultarchitecture.feature.domain.model.Feature

interface FeatureRepository {

    suspend fun getFeatures(): List<Feature>
}
