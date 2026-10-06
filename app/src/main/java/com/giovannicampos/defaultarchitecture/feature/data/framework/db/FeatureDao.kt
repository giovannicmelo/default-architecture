package com.giovannicampos.defaultarchitecture.feature.data.framework.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.giovannicampos.defaultarchitecture.feature.data.model.FeatureEntity

@Dao
interface FeatureDao {

    @Query("SELECT * FROM features")
    suspend fun getAll(): List<FeatureEntity>

    @Upsert
    suspend fun upsertAll(features: List<FeatureEntity>)
}
