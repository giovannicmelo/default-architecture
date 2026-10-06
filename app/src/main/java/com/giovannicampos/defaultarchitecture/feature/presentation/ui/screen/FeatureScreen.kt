package com.giovannicampos.defaultarchitecture.feature.presentation.ui.screen

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
import com.giovannicampos.defaultarchitecture.feature.domain.model.Feature
import com.giovannicampos.defaultarchitecture.feature.presentation.ui.effect.FeatureUiEffect
import com.giovannicampos.defaultarchitecture.feature.presentation.ui.intent.FeatureUiIntent
import com.giovannicampos.defaultarchitecture.feature.presentation.ui.state.FeatureUiState
import com.giovannicampos.defaultarchitecture.feature.presentation.viewmodel.FeatureViewModel
import kotlinx.coroutines.launch

@Composable
fun FeatureRoute(
    onNavigateToFeatureDetail: (featureId: String) -> Unit,
    viewModel: FeatureViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val loadErrorMessage = stringResource(R.string.feature_load_error)
    val currentOnNavigateToFeatureDetail by rememberUpdatedState(onNavigateToFeatureDetail)
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(viewModel, lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.uiEffect.collect { effect ->
                when (effect) {
                    is FeatureUiEffect.NavigateToFeatureDetail -> currentOnNavigateToFeatureDetail(effect.featureId)
                    FeatureUiEffect.ShowLoadError -> this@repeatOnLifecycle.launch {
                        snackbarHostState.showSnackbar(
                            loadErrorMessage
                        )
                    }
                }
            }
        }
    }

    FeatureScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onIntent = viewModel::onIntent,
    )
}

@Composable
fun FeatureScreen(
    uiState: FeatureUiState,
    snackbarHostState: SnackbarHostState,
    onIntent: (FeatureUiIntent) -> Unit,
) {
    val items = remember(uiState.features) {
        uiState.features.map { ItemListEntry(id = it.id, title = it.name, subtitle = it.id) }
    }

    ScreenTemplate(
        topBar = { AppTopBar(title = stringResource(R.string.feature_list_title)) },
        isLoading = uiState.isLoading,
        snackbarHostState = snackbarHostState,
    ) {
        ItemList(
            items = items,
            onItemClick = { onIntent(FeatureUiIntent.FeatureClicked(it)) },
        )
    }
}

@Preview
@Composable
private fun FeatureScreenPreview() {
    DefaultArchitectureTheme {
        FeatureScreen(
            uiState = FeatureUiState(
                features = listOf(Feature(id = "1", name = "Primeira"), Feature(id = "2", name = "Segunda")),
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onIntent = {},
        )
    }
}
