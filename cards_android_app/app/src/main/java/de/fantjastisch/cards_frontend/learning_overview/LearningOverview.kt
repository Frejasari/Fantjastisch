package de.fantjastisch.cards_frontend.learning_overview

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import de.fantjastisch.cards_frontend.glossary.LearningOverviewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun LearningOverview(
    modifier: Modifier = Modifier
) {

    val viewModel = viewModel { LearningOverviewModel() }
    LaunchedEffect(
        // wenn sich diese Variable ändert
        key1 = Unit,
        // dann wird dieses Lambda ausgeführt.
        block = {
            viewModel.onPageLoaded()
        })
    // Ein RecyclerView -> Eine lange liste von Eintraegen
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        items(viewModel.learningObjects.value) { learningObject ->
            Surface(
                modifier = Modifier,
                shadowElevation = 6.dp,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 16.dp),

                    ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier
                                .weight(weight = 1f, fill = false),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            text = learningObject.label
                        )
//                        CardContextMenu(
//                            cardId = learningObject.id,
//                            tag = card.tag
//                        )
                        // TODO: Allgemeine komponente für alle?
                    }

                    Divider(
                        modifier = Modifier
                            .padding(vertical = 6.dp)
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier,
                            text = viewModel.getLearningSystemLabel(learningObject.learningSystemId)
                        )
                        Spacer(
                            Modifier
                                .weight(1f)
                                .fillMaxHeight())
                        SuggestionChip(
                            modifier = Modifier,
                            onClick = { },
                            label = {
                                Text(
                                    modifier = Modifier,
                                    color =  if (viewModel.getProgressFromLearningObject(learningObject.id) < 33) Color(0xFFC53030)
                                        else if (viewModel.getProgressFromLearningObject(learningObject.id) < 66) Color(0xFFFF8707)
                                        else Color(0xFF2B990D),
                                    text = viewModel.getProgressFromLearningObject(learningObject.id).toString() + " %"
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

