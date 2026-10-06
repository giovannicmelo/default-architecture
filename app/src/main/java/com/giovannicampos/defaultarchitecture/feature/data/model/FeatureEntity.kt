package com.giovannicampos.defaultarchitecture.feature.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "features")
data class FeatureEntity(
    @PrimaryKey val id: String,
    val name: String,
)
