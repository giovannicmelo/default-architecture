package com.giovannicampos.defaultarchitecture.feature.data.source.local

import com.giovannicampos.defaultarchitecture.feature.data.framework.db.FeatureDao
import com.giovannicampos.defaultarchitecture.feature.data.model.FeatureEntity
import javax.inject.Inject

class FeatureLocalDataSourceImpl @Inject constructor(
    private val dao: FeatureDao,
) : FeatureLocalDataSource {

    override suspend fun getFeatures(): List<FeatureEntity> = dao.getAll()

    override suspend fun saveFeatures(features: List<FeatureEntity>) = dao.upsertAll(features)
}
