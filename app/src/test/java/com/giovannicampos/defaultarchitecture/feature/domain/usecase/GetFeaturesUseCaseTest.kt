package com.giovannicampos.defaultarchitecture.feature.domain.usecase

import com.giovannicampos.defaultarchitecture.feature.domain.model.Feature
import com.giovannicampos.defaultarchitecture.feature.domain.repository.FeatureRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetFeaturesUseCaseTest {

    private val repository = mockk<FeatureRepository>()
    private val getFeatures = GetFeaturesUseCase(repository)

    @Test
    fun `given repository has features, when invoked, then returns them`() = runTest {
        // Given
        val features = listOf(Feature(id = "1", name = "Primeira"))
        coEvery { repository.getFeatures() } returns features

        // When
        val result = getFeatures()

        // Then
        assertEquals(features, result)
    }
}
