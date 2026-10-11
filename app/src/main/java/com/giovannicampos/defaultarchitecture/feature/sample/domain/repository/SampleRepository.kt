package com.giovannicampos.defaultarchitecture.feature.sample.domain.repository

import com.giovannicampos.defaultarchitecture.feature.sample.domain.model.Sample

interface SampleRepository {

    suspend fun getSamples(): List<Sample>
}
