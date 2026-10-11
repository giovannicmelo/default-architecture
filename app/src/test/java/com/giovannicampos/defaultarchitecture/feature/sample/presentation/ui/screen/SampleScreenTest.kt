package com.giovannicampos.defaultarchitecture.feature.sample.presentation.ui.screen

import androidx.compose.ui.test.junit4.v2.createComposeRule
import com.giovannicampos.defaultarchitecture.feature.sample.domain.model.Sample
import com.giovannicampos.defaultarchitecture.feature.sample.presentation.ui.intent.SampleUiIntent
import com.giovannicampos.defaultarchitecture.feature.sample.presentation.ui.state.SampleUiState
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SampleScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val robot = SampleScreenRobot(composeRule)
    private val samples = listOf(Sample(id = "1", name = "Primeira"), Sample(id = "2", name = "Segunda"))

    @Test
    fun `given state with samples, when screen is shown, then displays each feature`() {
        // Given
        val uiState = SampleUiState(samples = samples)

        // When
        robot.showScreen(uiState)

        // Then
        robot
            .assertSampleDisplayed("Primeira")
            .assertSampleDisplayed("Segunda")
            .assertLoadingNotDisplayed()
    }

    @Test
    fun `given loading state, when screen is shown, then displays loading indicator`() {
        // Given
        val uiState = SampleUiState(isLoading = true)

        // When
        robot.showScreen(uiState)

        // Then
        robot.assertLoadingDisplayed()
    }

    @Test
    fun `given samples are displayed, when a feature is clicked, then sends feature clicked intent`() {
        // Given
        robot.showScreen(SampleUiState(samples = samples))

        // When
        robot.clickSample("Segunda")

        // Then
        robot.assertIntentsSent(SampleUiIntent.SampleClicked(sampleId = "2"))
    }
}
