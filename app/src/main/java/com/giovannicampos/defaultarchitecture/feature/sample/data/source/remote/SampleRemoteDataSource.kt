package com.giovannicampos.defaultarchitecture.feature.sample.data.source.remote

import com.giovannicampos.defaultarchitecture.feature.sample.data.model.SampleResponse

interface SampleRemoteDataSource {

    suspend fun fetchSamples(): List<SampleResponse>
}
