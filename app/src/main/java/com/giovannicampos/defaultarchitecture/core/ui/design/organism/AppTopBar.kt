package com.giovannicampos.defaultarchitecture.core.ui.design.organism

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.giovannicampos.defaultarchitecture.core.ui.design.atom.TitleText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title: String,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = { TitleText(text = title) },
        modifier = modifier,
    )
}
