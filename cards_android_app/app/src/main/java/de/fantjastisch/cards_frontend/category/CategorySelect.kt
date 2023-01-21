package de.fantjastisch.cards_frontend.category

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import java.util.*

data class CategorySelectItem(val label: String, val id: UUID, val isChecked: Boolean)

@Composable
fun CategorySelect(
    modifier: Modifier = Modifier,
    categories: List<CategorySelectItem>,
    onCategorySelected: (UUID) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        categories.map { category ->
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .weight(1f),
                    text = category.label
                )
                Checkbox(
                    checked = category.isChecked,
                    onCheckedChange = { onCategorySelected(category.id) }
                )
            }
        }
    }
}