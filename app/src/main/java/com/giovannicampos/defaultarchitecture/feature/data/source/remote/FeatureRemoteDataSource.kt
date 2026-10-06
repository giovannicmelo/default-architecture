package com.giovannicampos.defaultarchitecture.feature.data.source.remote

import com.giovannicampos.defaultarchitecture.feature.data.model.FeatureResponse

interface FeatureRemoteDataSource {

    suspend fun fetchFeatures(): List<FeatureResponse>
}
