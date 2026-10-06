package com.giovannicampos.defaultarchitecture.core.ui.design.molecule

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.giovannicampos.defaultarchitecture.core.ui.design.atom.BodyText
import com.giovannicampos.defaultarchitecture.core.ui.design.atom.TitleText
import com.giovannicampos.defaultarchitecture.core.ui.theme.DefaultArchitectureTheme
import com.giovannicampos.defaultarchitecture.core.ui.theme.Spacing

@Composable
fun ListItemCard(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(modifier = Modifier.padding(Spacing.large)) {
            TitleText(text = title)
            if (subtitle != null) {
                BodyText(text = subtitle, modifier = Modifier.padding(top = Spacing.small))
            }
        }
    }
}

@Preview
@Composable
private fun ListItemCardPreview() {
    DefaultArchitectureTheme {
        ListItemCard(title = "Título", subtitle = "Subtítulo", onClick = {})
    }
}
