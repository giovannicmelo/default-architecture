package com.giovannicampos.defaultarchitecture.feature.sample.di

import com.giovannicampos.defaultarchitecture.feature.sample.domain.model.Sample
import com.giovannicampos.defaultarchitecture.feature.sample.domain.repository.SampleRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeSampleRepository @Inject constructor() : SampleRepository {

    var samples: List<Sample> = emptyList()
    var error: Throwable? = null

    override suspend fun getSamples(): List<Sample> = error?.let { throw it } ?: samples
}
