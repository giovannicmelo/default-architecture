package com.giovannicampos.defaultarchitecture.feature.presentation.viewmodel

import app.cash.turbine.test
import com.giovannicampos.defaultarchitecture.core.testing.MainDispatcherRule
import com.giovannicampos.defaultarchitecture.feature.domain.model.Feature
import com.giovannicampos.defaultarchitecture.feature.domain.usecase.GetFeaturesUseCase
import com.giovannicampos.defaultarchitecture.feature.presentation.ui.effect.FeatureUiEffect
import com.giovannicampos.defaultarchitecture.feature.presentation.ui.intent.FeatureUiIntent
import com.giovannicampos.defaultarchitecture.feature.presentation.ui.state.FeatureUiState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test
import java.io.IOException

class FeatureViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getFeatures = mockk<GetFeaturesUseCase>()
    private val features = listOf(Feature(id = "1", name = "Primeira"))

    @Test
    fun `given use case is still loading, when view model is created, then state is loading`() = runTest {
        // Given
        coEvery { getFeatures() } coAnswers { awaitCancellation() }

        // When
        val viewModel = FeatureViewModel(getFeatures)

        // Then
        viewModel.uiState.test {
            assertEquals(FeatureUiState(isLoading = true), awaitItem())
        }
    }

    @Test
    fun `given use case returns features, when view model is created, then state contains features`() = runTest {
        // Given
        coEvery { getFeatures() } returns features

        // When
        val viewModel = FeatureViewModel(getFeatures)

        // Then
        viewModel.uiState.test {
            assertEquals(FeatureUiState(isLoading = false, features = features), awaitItem())
        }
    }

    @Test
    fun `given use case fails, when view model is created, then emits load error and stops loading`() = runTest {
        // Given
        coEvery { getFeatures() } throws IOException()

        // When
        val viewModel = FeatureViewModel(getFeatures)

        // Then
        viewModel.uiEffect.test {
            assertEquals(FeatureUiEffect.ShowLoadError, awaitItem())
        }
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `given first load failed, when load features is sent again, then state contains features`() = runTest {
        // Given
        coEvery { getFeatures() } throws IOException() andThen features
        val viewModel = FeatureViewModel(getFeatures)

        // When
        viewModel.onIntent(FeatureUiIntent.LoadFeatures)

        // Then
        viewModel.uiState.test {
            assertEquals(FeatureUiState(isLoading = false, features = features), awaitItem())
        }
    }

    @Test
    fun `given features are loaded, when a feature is clicked, then emits navigation to its detail`() = runTest {
        // Given
        coEvery { getFeatures() } returns features
        val viewModel = FeatureViewModel(getFeatures)

        viewModel.uiEffect.test {
            // When
            viewModel.onIntent(FeatureUiIntent.FeatureClicked(featureId = "1"))

            // Then
            assertEquals(FeatureUiEffect.NavigateToFeatureDetail(featureId = "1"), awaitItem())
        }
    }
}
