package com.giovannicampos.defaultarchitecture.feature.data.repository

import com.giovannicampos.defaultarchitecture.feature.data.mapper.toDomain
import com.giovannicampos.defaultarchitecture.feature.data.mapper.toEntity
import com.giovannicampos.defaultarchitecture.feature.data.source.local.FeatureLocalDataSource
import com.giovannicampos.defaultarchitecture.feature.data.source.remote.FeatureRemoteDataSource
import com.giovannicampos.defaultarchitecture.feature.domain.model.Feature
import com.giovannicampos.defaultarchitecture.feature.domain.repository.FeatureRepository
import java.io.IOException
import javax.inject.Inject

class FeatureRepositoryImpl @Inject constructor(
    private val remoteDataSource: FeatureRemoteDataSource,
    private val localDataSource: FeatureLocalDataSource,
) : FeatureRepository {

    override suspend fun getFeatures(): List<Feature> {
        try {
            val features = remoteDataSource.fetchFeatures().map { it.toEntity() }
            localDataSource.saveFeatures(features)
        } catch (e: IOException) {
            // Sem rede: segue com o que já está salvo localmente.
        }
        return localDataSource.getFeatures().map { it.toDomain() }
    }
}
