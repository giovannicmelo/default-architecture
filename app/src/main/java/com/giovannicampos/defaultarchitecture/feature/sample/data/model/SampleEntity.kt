package com.giovannicampos.defaultarchitecture.feature.sample.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "samples")
data class SampleEntity(
    @PrimaryKey val id: String,
    val name: String,
)
