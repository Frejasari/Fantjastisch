package de.fantjastisch.cards_frontend.learning_system

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.util.*

data class LearningSystemSelectItem(val label: String, val id: UUID, val boxLabels: List<String>, val isChecked: Boolean)

@Composable
fun LearningSystemSelect(
    modifier: Modifier = Modifier,
    learningSystems: List<LearningSystemSelectItem>,
    onLearningSystemSelected: (UUID) -> Unit = {}
) {
    // Ein RecyclerView -> Eine lange liste von Eintraegen
    LazyColumn(
        modifier = modifier
            .fillMaxWidth(),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        items(learningSystems) { learningSystem ->
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .weight(1f),
                    text = learningSystem.label
                )
                Checkbox(
                    checked = learningSystem.isChecked,
                    onCheckedChange = { onLearningSystemSelected(learningSystem.id) }
                )
            }
        }
    }
}