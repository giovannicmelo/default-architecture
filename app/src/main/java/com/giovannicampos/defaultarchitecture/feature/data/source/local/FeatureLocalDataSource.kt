package com.giovannicampos.defaultarchitecture.feature.data.source.local

import com.giovannicampos.defaultarchitecture.feature.data.model.FeatureEntity

interface FeatureLocalDataSource {

    suspend fun getFeatures(): List<FeatureEntity>

    suspend fun saveFeatures(features: List<FeatureEntity>)
}
