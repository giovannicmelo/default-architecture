package com.giovannicampos.defaultarchitecture.feature.presentation

import androidx.compose.ui.test.junit4.v2.createEmptyComposeRule
import com.giovannicampos.defaultarchitecture.R
import com.giovannicampos.defaultarchitecture.feature.di.FakeFeatureRepository
import com.giovannicampos.defaultarchitecture.feature.domain.model.Feature
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException
import javax.inject.Inject

@HiltAndroidTest
class FeatureFlowTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createEmptyComposeRule()

    @Inject
    lateinit var repository: FakeFeatureRepository

    private val robot = FeatureFlowRobot(composeRule)

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun givenRepositoryHasFeatures_whenAppIsOpened_thenShowsFeatureList() {
        // Given
        repository.features = listOf(Feature(id = "1", name = "Primeira"), Feature(id = "2", name = "Segunda"))

        // When
        robot.openApp()

        // Then
        robot
            .assertTextDisplayed(R.string.feature_list_title)
            .assertTextDisplayed("Primeira")
            .assertTextDisplayed("Segunda")
    }

    @Test
    fun givenRepositoryFails_whenAppIsOpened_thenShowsLoadErrorMessage() {
        // Given
        repository.error = IOException()

        // When
        robot.openApp()

        // Then
        robot.assertTextDisplayed(R.string.feature_load_error)
    }
}
