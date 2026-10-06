package com.giovannicampos.defaultarchitecture.feature.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.giovannicampos.defaultarchitecture.feature.domain.usecase.GetFeaturesUseCase
import com.giovannicampos.defaultarchitecture.feature.presentation.ui.effect.FeatureUiEffect
import com.giovannicampos.defaultarchitecture.feature.presentation.ui.intent.FeatureUiIntent
import com.giovannicampos.defaultarchitecture.feature.presentation.ui.state.FeatureUiState
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
class FeatureViewModel @Inject constructor(
    private val getFeatures: GetFeaturesUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(FeatureUiState())
    val uiState: StateFlow<FeatureUiState> = _uiState.asStateFlow()

    private val _uiEffect = Channel<FeatureUiEffect>(Channel.BUFFERED)
    val uiEffect: Flow<FeatureUiEffect> = _uiEffect.receiveAsFlow()

    init {
        onIntent(FeatureUiIntent.LoadFeatures)
    }

    fun onIntent(intent: FeatureUiIntent) {
        when (intent) {
            FeatureUiIntent.LoadFeatures -> loadFeatures()
            is FeatureUiIntent.FeatureClicked -> sendEffect(FeatureUiEffect.NavigateToFeatureDetail(intent.featureId))
        }
    }

    private fun loadFeatures() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val features = getFeatures()
                _uiState.update { it.copy(isLoading = false, features = features) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
                _uiEffect.send(FeatureUiEffect.ShowLoadError)
            }
        }
    }

    private fun sendEffect(effect: FeatureUiEffect) {
        viewModelScope.launch { _uiEffect.send(effect) }
    }
}
