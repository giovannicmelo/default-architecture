package com.giovannicampos.defaultarchitecture.feature.sample.data.repository

import com.giovannicampos.defaultarchitecture.feature.sample.data.mapper.toDomain
import com.giovannicampos.defaultarchitecture.feature.sample.data.mapper.toEntity
import com.giovannicampos.defaultarchitecture.feature.sample.data.source.local.SampleLocalDataSource
import com.giovannicampos.defaultarchitecture.feature.sample.data.source.remote.SampleRemoteDataSource
import com.giovannicampos.defaultarchitecture.feature.sample.domain.model.Sample
import com.giovannicampos.defaultarchitecture.feature.sample.domain.repository.SampleRepository
import java.io.IOException
import javax.inject.Inject

class SampleRepositoryImpl @Inject constructor(
    private val remoteDataSource: SampleRemoteDataSource,
    private val localDataSource: SampleLocalDataSource,
) : SampleRepository {

    override suspend fun getSamples(): List<Sample> {
        try {
            val samples = remoteDataSource.fetchSamples().map { it.toEntity() }
            localDataSource.saveSamples(samples)
        } catch (e: IOException) {
            // Sem rede: segue com o que já está salvo localmente.
        }
        return localDataSource.getSamples().map { it.toDomain() }
    }
}
