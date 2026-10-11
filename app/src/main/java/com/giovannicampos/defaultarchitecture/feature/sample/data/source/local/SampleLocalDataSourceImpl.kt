package com.giovannicampos.defaultarchitecture.feature.sample.data.source.local

import com.giovannicampos.defaultarchitecture.feature.sample.data.framework.db.SampleDao
import com.giovannicampos.defaultarchitecture.feature.sample.data.model.SampleEntity
import javax.inject.Inject

class SampleLocalDataSourceImpl @Inject constructor(
    private val dao: SampleDao,
) : SampleLocalDataSource {

    override suspend fun getSamples(): List<SampleEntity> = dao.getAll()

    override suspend fun saveSamples(samples: List<SampleEntity>) = dao.upsertAll(samples)
}
