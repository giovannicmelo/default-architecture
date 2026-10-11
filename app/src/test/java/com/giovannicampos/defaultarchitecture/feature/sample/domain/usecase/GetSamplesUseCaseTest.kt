package com.giovannicampos.defaultarchitecture.feature.sample.domain.usecase

import com.giovannicampos.defaultarchitecture.feature.sample.domain.model.Sample
import com.giovannicampos.defaultarchitecture.feature.sample.domain.repository.SampleRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetSamplesUseCaseTest {

    private val repository = mockk<SampleRepository>()
    private val getSamples = GetSamplesUseCase(repository)

    @Test
    fun `given repository has samples, when invoked, then returns them`() = runTest {
        // Given
        val samples = listOf(Sample(id = "1", name = "Primeira"))
        coEvery { repository.getSamples() } returns samples

        // When
        val result = getSamples()

        // Then
        assertEquals(samples, result)
    }
}
