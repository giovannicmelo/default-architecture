package com.giovannicampos.defaultarchitecture.feature.sample.data.model

import kotlinx.serialization.Serializable

@Serializable
data class SampleResponse(
    val id: String,
    val name: String,
)
