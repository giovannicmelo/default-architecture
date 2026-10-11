package com.giovannicampos.defaultarchitecture.feature.sample.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.giovannicampos.defaultarchitecture.feature.sample.domain.usecase.GetSamplesUseCase
import com.giovannicampos.defaultarchitecture.feature.sample.presentation.ui.effect.SampleUiEffect
import com.giovannicampos.defaultarchitecture.feature.sample.presentation.ui.intent.SampleUiIntent
import com.giovannicampos.defaultarchitecture.feature.sample.presentation.ui.state.SampleUiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class SampleViewModel @Inject constructor(
    private val getSamples: GetSamplesUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SampleUiState())
    val uiState: StateFlow<SampleUiState> = _uiState.asStateFlow()

    private val _uiEffect = Channel<SampleUiEffect>(Channel.BUFFERED)
    val uiEffect: Flow<SampleUiEffect> = _uiEffect.receiveAsFlow()

    init {
        onIntent(SampleUiIntent.LoadSamples)
    }

    fun onIntent(intent: SampleUiIntent) {
        when (intent) {
            SampleUiIntent.LoadSamples -> loadSamples()
            is SampleUiIntent.SampleClicked -> sendEffect(SampleUiEffect.NavigateToSampleDetail(intent.sampleId))
        }
    }

    private fun loadSamples() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val samples = getSamples()
                _uiState.update { it.copy(isLoading = false, samples = samples) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
                _uiEffect.send(SampleUiEffect.ShowLoadError)
            }
        }
    }

    private fun sendEffect(effect: SampleUiEffect) {
        viewModelScope.launch { _uiEffect.send(effect) }
    }
}
