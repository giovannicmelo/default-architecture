package com.giovannicampos.defaultarchitecture.feature.presentation.ui.screen

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.giovannicampos.defaultarchitecture.core.ui.theme.DefaultArchitectureTheme
import com.giovannicampos.defaultarchitecture.feature.presentation.ui.intent.FeatureUiIntent
import com.giovannicampos.defaultarchitecture.feature.presentation.ui.state.FeatureUiState
import org.junit.Assert.assertEquals

class FeatureScreenRobot(private val composeRule: ComposeContentTestRule) {

    private val sentIntents = mutableListOf<FeatureUiIntent>()

    fun showScreen(uiState: FeatureUiState) = apply {
        composeRule.setContent {
            DefaultArchitectureTheme {
                FeatureScreen(
                    uiState = uiState,
                    snackbarHostState = remember { SnackbarHostState() },
                    onIntent = { sentIntents += it },
                )
            }
        }
    }

    fun clickFeature(name: String) = apply {
        composeRule.onNodeWithText(name).performClick()
    }

    fun assertFeatureDisplayed(name: String) = apply {
        composeRule.onNodeWithText(name).assertIsDisplayed()
    }

    fun assertLoadingDisplayed() = apply {
        composeRule.onNode(loadingIndicator).assertIsDisplayed()
    }

    fun assertLoadingNotDisplayed() = apply {
        composeRule.onNode(loadingIndicator).assertDoesNotExist()
    }

    fun assertIntentsSent(vararg intents: FeatureUiIntent) = apply {
        assertEquals(intents.toList(), sentIntents)
    }

    private companion object {
        val loadingIndicator = hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate)
    }
}
