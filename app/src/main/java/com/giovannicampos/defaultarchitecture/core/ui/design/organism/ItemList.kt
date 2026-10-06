package com.giovannicampos.defaultarchitecture.core.ui.design.organism

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.giovannicampos.defaultarchitecture.core.ui.design.molecule.ListItemCard
import com.giovannicampos.defaultarchitecture.core.ui.theme.Spacing

data class ItemListEntry(
    val id: String,
    val title: String,
    val subtitle: String? = null,
)

@Composable
fun ItemList(
    items: List<ItemListEntry>,
    onItemClick: (id: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(Spacing.large),
        verticalArrangement = Arrangement.spacedBy(Spacing.medium),
    ) {
        items(items = items, key = { it.id }) { item ->
            ListItemCard(
                title = item.title,
                subtitle = item.subtitle,
                onClick = { onItemClick(item.id) },
            )
        }
    }
}
