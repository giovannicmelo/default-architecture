package com.giovannicampos.defaultarchitecture.feature.data.mapper

import com.giovannicampos.defaultarchitecture.feature.data.model.FeatureEntity
import com.giovannicampos.defaultarchitecture.feature.data.model.FeatureResponse
import com.giovannicampos.defaultarchitecture.feature.domain.model.Feature

fun FeatureResponse.toEntity() = FeatureEntity(
    id = id,
    name = name,
)

fun FeatureEntity.toDomain() = Feature(
    id = id,
    name = name,
)
