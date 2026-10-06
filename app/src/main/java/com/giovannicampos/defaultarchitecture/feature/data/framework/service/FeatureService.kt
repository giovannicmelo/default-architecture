package com.giovannicampos.defaultarchitecture.feature.data.framework.service

import com.giovannicampos.defaultarchitecture.feature.data.model.FeatureResponse
import retrofit2.http.GET

interface FeatureService {

    @GET("features")
    suspend fun getFeatures(): List<FeatureResponse>
}
