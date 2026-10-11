package com.giovannicampos.defaultarchitecture.feature.sample.data.framework.service

import com.giovannicampos.defaultarchitecture.feature.sample.data.model.SampleResponse
import retrofit2.http.GET

interface SampleService {

    @GET("samples")
    suspend fun getSamples(): List<SampleResponse>
}
