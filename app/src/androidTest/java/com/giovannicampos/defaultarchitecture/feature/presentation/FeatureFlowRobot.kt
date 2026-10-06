package com.giovannicampos.defaultarchitecture.feature.presentation

import androidx.annotation.StringRes
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.core.app.ActivityScenario
import androidx.test.platform.app.InstrumentationRegistry
import com.giovannicampos.defaultarchitecture.MainActivity

class FeatureFlowRobot(private val composeRule: ComposeTestRule) {

    fun openApp() = apply {
        ActivityScenario.launch(MainActivity::class.java)
    }

    fun assertTextDisplayed(text: String) = apply {
        composeRule.onNodeWithText(text).assertIsDisplayed()
    }

    fun assertTextDisplayed(@StringRes textRes: Int) = assertTextDisplayed(
        InstrumentationRegistry.getInstrumentation().targetContext.getString(textRes),
    )
}
