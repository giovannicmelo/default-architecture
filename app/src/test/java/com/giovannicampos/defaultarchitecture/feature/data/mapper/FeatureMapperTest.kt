package com.giovannicampos.defaultarchitecture.feature.data.mapper

import com.giovannicampos.defaultarchitecture.feature.data.model.FeatureEntity
import com.giovannicampos.defaultarchitecture.feature.data.model.FeatureResponse
import com.giovannicampos.defaultarchitecture.feature.domain.model.Feature
import org.junit.Assert.assertEquals
import org.junit.Test

class FeatureMapperTest {

    @Test
    fun `given a feature response, when mapped to entity, then keeps id and name`() {
        // Given
        val response = FeatureResponse(id = "1", name = "Primeira")

        // When
        val entity = response.toEntity()

        // Then
        assertEquals(FeatureEntity(id = "1", name = "Primeira"), entity)
    }

    @Test
    fun `given a feature entity, when mapped to domain, then keeps id and name`() {
        // Given
        val entity = FeatureEntity(id = "1", name = "Primeira")

        // When
        val feature = entity.toDomain()

        // Then
        assertEquals(Feature(id = "1", name = "Primeira"), feature)
    }
}
