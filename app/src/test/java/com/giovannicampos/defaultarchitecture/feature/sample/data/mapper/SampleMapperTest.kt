package com.giovannicampos.defaultarchitecture.feature.sample.data.mapper

import com.giovannicampos.defaultarchitecture.feature.sample.data.model.SampleEntity
import com.giovannicampos.defaultarchitecture.feature.sample.data.model.SampleResponse
import com.giovannicampos.defaultarchitecture.feature.sample.domain.model.Sample
import org.junit.Assert.assertEquals
import org.junit.Test

class SampleMapperTest {

    @Test
    fun `given a feature response, when mapped to entity, then keeps id and name`() {
        // Given
        val response = SampleResponse(id = "1", name = "Primeira")

        // When
        val entity = response.toEntity()

        // Then
        assertEquals(SampleEntity(id = "1", name = "Primeira"), entity)
    }

    @Test
    fun `given a feature entity, when mapped to domain, then keeps id and name`() {
        // Given
        val entity = SampleEntity(id = "1", name = "Primeira")

        // When
        val feature = entity.toDomain()

        // Then
        assertEquals(Sample(id = "1", name = "Primeira"), feature)
    }
}
