package com.giovannicampos.defaultarchitecture.feature.sample.data.source.local

import com.giovannicampos.defaultarchitecture.feature.sample.data.model.SampleEntity

interface SampleLocalDataSource {

    suspend fun getSamples(): List<SampleEntity>

    suspend fun saveSamples(samples: List<SampleEntity>)
}
