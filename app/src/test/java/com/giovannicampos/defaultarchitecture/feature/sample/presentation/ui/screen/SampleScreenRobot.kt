package com.giovannicampos.defaultarchitecture.feature.sample.presentation.ui.screen

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.giovannicampos.defaultarchitecture.core.ui.theme.DefaultArchitectureTheme
import com.giovannicampos.defaultarchitecture.feature.sample.presentation.ui.intent.SampleUiIntent
import com.giovannicampos.defaultarchitecture.feature.sample.presentation.ui.state.SampleUiState
import org.junit.Assert.assertEquals

class SampleScreenRobot(private val composeRule: ComposeContentTestRule) {

    private val sentIntents = mutableListOf<SampleUiIntent>()

    fun showScreen(uiState: SampleUiState) = apply {
        composeRule.setContent {
            DefaultArchitectureTheme {
                SampleScreen(
                    uiState = uiState,
                    snackbarHostState = remember { SnackbarHostState() },
                    onIntent = { sentIntents += it },
                )
            }
        }
    }

    fun clickSample(name: String) = apply {
        composeRule.onNodeWithText(name).performClick()
    }

    fun assertSampleDisplayed(name: String) = apply {
        composeRule.onNodeWithText(name).assertIsDisplayed()
    }

    fun assertLoadingDisplayed() = apply {
        composeRule.onNode(loadingIndicator).assertIsDisplayed()
    }

    fun assertLoadingNotDisplayed() = apply {
        composeRule.onNode(loadingIndicator).assertDoesNotExist()
    }

    fun assertIntentsSent(vararg intents: SampleUiIntent) = apply {
        assertEquals(intents.toList(), sentIntents)
    }

    private companion object {
        val loadingIndicator = hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate)
    }
}
