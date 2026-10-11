package com.giovannicampos.defaultarchitecture.feature.sample.data.source.remote

import com.giovannicampos.defaultarchitecture.feature.sample.data.framework.service.SampleService
import com.giovannicampos.defaultarchitecture.feature.sample.data.model.SampleResponse
import javax.inject.Inject

class SampleRemoteDataSourceImpl @Inject constructor(
    private val service: SampleService,
) : SampleRemoteDataSource {

    override suspend fun fetchSamples(): List<SampleResponse> = service.getSamples()
}
