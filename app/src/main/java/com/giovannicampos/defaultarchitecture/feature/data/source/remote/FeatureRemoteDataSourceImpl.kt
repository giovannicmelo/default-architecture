package com.giovannicampos.defaultarchitecture.feature.data.source.remote

import com.giovannicampos.defaultarchitecture.feature.data.framework.service.FeatureService
import com.giovannicampos.defaultarchitecture.feature.data.model.FeatureResponse
import javax.inject.Inject

class FeatureRemoteDataSourceImpl @Inject constructor(
    private val service: FeatureService,
) : FeatureRemoteDataSource {

    override suspend fun fetchFeatures(): List<FeatureResponse> = service.getFeatures()
}
