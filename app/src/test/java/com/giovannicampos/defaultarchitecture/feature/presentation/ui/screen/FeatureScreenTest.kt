package com.giovannicampos.defaultarchitecture.feature.presentation.ui.screen

import androidx.compose.ui.test.junit4.v2.createComposeRule
import com.giovannicampos.defaultarchitecture.feature.domain.model.Feature
import com.giovannicampos.defaultarchitecture.feature.presentation.ui.intent.FeatureUiIntent
import com.giovannicampos.defaultarchitecture.feature.presentation.ui.state.FeatureUiState
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class FeatureScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val robot = FeatureScreenRobot(composeRule)
    private val features = listOf(Feature(id = "1", name = "Primeira"), Feature(id = "2", name = "Segunda"))

    @Test
    fun `given state with features, when screen is shown, then displays each feature`() {
        // Given
        val uiState = FeatureUiState(features = features)

        // When
        robot.showScreen(uiState)

        // Then
        robot
            .assertFeatureDisplayed("Primeira")
            .assertFeatureDisplayed("Segunda")
            .assertLoadingNotDisplayed()
    }

    @Test
    fun `given loading state, when screen is shown, then displays loading indicator`() {
        // Given
        val uiState = FeatureUiState(isLoading = true)

        // When
        robot.showScreen(uiState)

        // Then
        robot.assertLoadingDisplayed()
    }

    @Test
    fun `given features are displayed, when a feature is clicked, then sends feature clicked intent`() {
        // Given
        robot.showScreen(FeatureUiState(features = features))

        // When
        robot.clickFeature("Segunda")

        // Then
        robot.assertIntentsSent(FeatureUiIntent.FeatureClicked(featureId = "2"))
    }
}
