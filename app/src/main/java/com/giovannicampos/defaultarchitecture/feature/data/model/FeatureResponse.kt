package com.giovannicampos.defaultarchitecture.feature.data.model

import kotlinx.serialization.Serializable

@Serializable
data class FeatureResponse(
    val id: String,
    val name: String,
)
