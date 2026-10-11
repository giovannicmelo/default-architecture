package com.giovannicampos.defaultarchitecture.feature.sample.data.framework.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.giovannicampos.defaultarchitecture.feature.sample.data.model.SampleEntity

@Dao
interface SampleDao {

    @Query("SELECT * FROM samples")
    suspend fun getAll(): List<SampleEntity>

    @Upsert
    suspend fun upsertAll(samples: List<SampleEntity>)
}
