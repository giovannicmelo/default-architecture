package com.giovannicampos.defaultarchitecture.feature.sample.presentation.viewmodel

import app.cash.turbine.test
import com.giovannicampos.defaultarchitecture.core.testing.MainDispatcherRule
import com.giovannicampos.defaultarchitecture.feature.sample.domain.model.Sample
import com.giovannicampos.defaultarchitecture.feature.sample.domain.usecase.GetSamplesUseCase
import com.giovannicampos.defaultarchitecture.feature.sample.presentation.ui.effect.SampleUiEffect
import com.giovannicampos.defaultarchitecture.feature.sample.presentation.ui.intent.SampleUiIntent
import com.giovannicampos.defaultarchitecture.feature.sample.presentation.ui.state.SampleUiState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test
import java.io.IOException

class SampleViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getSamples = mockk<GetSamplesUseCase>()
    private val samples = listOf(Sample(id = "1", name = "Primeira"))

    @Test
    fun `given use case is still loading, when view model is created, then state is loading`() = runTest {
        // Given
        coEvery { getSamples() } coAnswers { awaitCancellation() }

        // When
        val viewModel = SampleViewModel(getSamples)

        // Then
        viewModel.uiState.test {
            assertEquals(SampleUiState(isLoading = true), awaitItem())
        }
    }

    @Test
    fun `given use case returns samples, when view model is created, then state contains samples`() = runTest {
        // Given
        coEvery { getSamples() } returns samples

        // When
        val viewModel = SampleViewModel(getSamples)

        // Then
        viewModel.uiState.test {
            assertEquals(SampleUiState(isLoading = false, samples = samples), awaitItem())
        }
    }

    @Test
    fun `given use case fails, when view model is created, then emits load error and stops loading`() = runTest {
        // Given
        coEvery { getSamples() } throws IOException()

        // When
        val viewModel = SampleViewModel(getSamples)

        // Then
        viewModel.uiEffect.test {
            assertEquals(SampleUiEffect.ShowLoadError, awaitItem())
        }
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `given first load failed, when load samples is sent again, then state contains samples`() = runTest {
        // Given
        coEvery { getSamples() } throws IOException() andThen samples
        val viewModel = SampleViewModel(getSamples)

        // When
        viewModel.onIntent(SampleUiIntent.LoadSamples)

        // Then
        viewModel.uiState.test {
            assertEquals(SampleUiState(isLoading = false, samples = samples), awaitItem())
        }
    }

    @Test
    fun `given samples are loaded, when a sample is clicked, then emits navigation to its detail`() = runTest {
        // Given
        coEvery { getSamples() } returns samples
        val viewModel = SampleViewModel(getSamples)

        viewModel.uiEffect.test {
            // When
            viewModel.onIntent(SampleUiIntent.SampleClicked(sampleId = "1"))

            // Then
            assertEquals(SampleUiEffect.NavigateToSampleDetail(sampleId = "1"), awaitItem())
        }
    }
}
