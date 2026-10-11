package com.giovannicampos.defaultarchitecture.feature.sample.presentation

import androidx.compose.ui.test.junit4.v2.createEmptyComposeRule
import com.giovannicampos.defaultarchitecture.R
import com.giovannicampos.defaultarchitecture.feature.sample.di.FakeSampleRepository
import com.giovannicampos.defaultarchitecture.feature.sample.domain.model.Sample
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException
import javax.inject.Inject

@HiltAndroidTest
class SampleFlowTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createEmptyComposeRule()

    @Inject
    lateinit var repository: FakeSampleRepository

    private val robot = SampleFlowRobot(composeRule)

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun givenRepositoryHasSamples_whenAppIsOpened_thenShowsSampleList() {
        // Given
        repository.samples = listOf(Sample(id = "1", name = "Primeira"), Sample(id = "2", name = "Segunda"))

        // When
        robot.openApp()

        // Then
        robot
            .assertTextDisplayed(R.string.sample_list_title)
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
        robot.assertTextDisplayed(R.string.sample_load_error)
    }
}
