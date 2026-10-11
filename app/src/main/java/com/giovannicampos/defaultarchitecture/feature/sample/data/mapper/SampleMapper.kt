package com.giovannicampos.defaultarchitecture.feature.sample.data.mapper

import com.giovannicampos.defaultarchitecture.feature.sample.data.model.SampleEntity
import com.giovannicampos.defaultarchitecture.feature.sample.data.model.SampleResponse
import com.giovannicampos.defaultarchitecture.feature.sample.domain.model.Sample

fun SampleResponse.toEntity() = SampleEntity(
    id = id,
    name = name,
)

fun SampleEntity.toDomain() = Sample(
    id = id,
    name = name,
)
