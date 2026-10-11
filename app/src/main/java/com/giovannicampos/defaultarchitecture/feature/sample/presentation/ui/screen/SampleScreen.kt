package com.giovannicampos.defaultarchitecture.feature.sample.presentation.ui.screen

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.giovannicampos.defaultarchitecture.R
import com.giovannicampos.defaultarchitecture.core.ui.design.organism.AppTopBar
import com.giovannicampos.defaultarchitecture.core.ui.design.organism.ItemList
import com.giovannicampos.defaultarchitecture.core.ui.design.organism.ItemListEntry
import com.giovannicampos.defaultarchitecture.core.ui.design.template.ScreenTemplate
import com.giovannicampos.defaultarchitecture.core.ui.theme.DefaultArchitectureTheme
import com.giovannicampos.defaultarchitecture.feature.sample.domain.model.Sample
import com.giovannicampos.defaultarchitecture.feature.sample.presentation.ui.effect.SampleUiEffect
import com.giovannicampos.defaultarchitecture.feature.sample.presentation.ui.intent.SampleUiIntent
import com.giovannicampos.defaultarchitecture.feature.sample.presentation.ui.state.SampleUiState
import com.giovannicampos.defaultarchitecture.feature.sample.presentation.viewmodel.SampleViewModel
import kotlinx.coroutines.launch

@Composable
fun SampleRoute(
    onNavigateToSampleDetail: (sampleId: String) -> Unit,
    viewModel: SampleViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val loadErrorMessage = stringResource(R.string.sample_load_error)
    val currentOnNavigateToSampleDetail by rememberUpdatedState(onNavigateToSampleDetail)
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(viewModel, lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.uiEffect.collect { effect ->
                when (effect) {
                    is SampleUiEffect.NavigateToSampleDetail -> currentOnNavigateToSampleDetail(effect.sampleId)
                    SampleUiEffect.ShowLoadError -> this@repeatOnLifecycle.launch {
                        snackbarHostState.showSnackbar(
                            loadErrorMessage
                        )
                    }
                }
            }
        }
    }

    SampleScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onIntent = viewModel::onIntent,
    )
}

@Composable
fun SampleScreen(
    uiState: SampleUiState,
    snackbarHostState: SnackbarHostState,
    onIntent: (SampleUiIntent) -> Unit,
) {
    val items = remember(uiState.samples) {
        uiState.samples.map { ItemListEntry(id = it.id, title = it.name, subtitle = it.id) }
    }

    ScreenTemplate(
        topBar = { AppTopBar(title = stringResource(R.string.sample_list_title)) },
        isLoading = uiState.isLoading,
        snackbarHostState = snackbarHostState,
    ) {
        ItemList(
            items = items,
            onItemClick = { onIntent(SampleUiIntent.SampleClicked(it)) },
        )
    }
}

@Preview
@Composable
private fun SampleScreenPreview() {
    DefaultArchitectureTheme {
        SampleScreen(
            uiState = SampleUiState(
                samples = listOf(Sample(id = "1", name = "Primeira"), Sample(id = "2", name = "Segunda")),
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onIntent = {},
        )
    }
}
