package de.fantjastisch.cards_frontend.learning_overview.learning_object_component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import de.fantjastisch.cards_frontend.learning_object.LearningObject
import de.fantjastisch.cards_frontend.learning_object_details.LearningDetailsFragment
import de.fantjastisch.cards_frontend.learning_overview.delete.DeleteLearningObjectDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearningObjectComponent(
    learningObject: LearningObject,
    onDeleteSuccessful: () -> Unit
) {
    val navigator = LocalNavigator.current!!
    val viewModel = viewModel(key = learningObject.id.toString()) {
        LearningObjectComponentViewModel(
            learningSystemId = learningObject.learningSystemId,
            learningObjectId = learningObject.id
        )
    }
    val isDeleteDialogOpen = remember { mutableStateOf(false) }
    // Ein RecyclerView -> Eine lange liste von Eintraegen
    Box(Modifier.clickable(onClick = {
        navigator.push(
            LearningDetailsFragment(
                learningObject.id
            )
        )
    })) {
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
                            .weight(weight = 6f, fill = false),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        text = learningObject.label
                    )
                    IconButton(
                        modifier = Modifier
                            .weight(1f),
                        onClick = {
                            isDeleteDialogOpen.value = !isDeleteDialogOpen.value
                        }) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = "delete icon"
                        )
                    }
                    DeleteLearningObjectDialog(
                        learningObjectId = learningObject.id,
                        isOpen = isDeleteDialogOpen.value,
                        setIsOpen = { isDeleteDialogOpen.value = it },
                        onDeleteSuccessful = onDeleteSuccessful
                    )
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
                        text = viewModel.learningSystemLabel.value
                    )
                    Spacer(
                        Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    )

                    SuggestionChip(
                        modifier = Modifier,
                        onClick = { },
                        label = {
                            Text(
                                modifier = Modifier,
                                color = if (viewModel.progress.value < 33) Color(
                                    0xFFC53030
                                )
                                else if (viewModel.progress.value < 66)
                                    Color(0xFFFF8707)
                                else Color(0xFF2B990D),
                                text = if (viewModel.progress.value == -1) {
                                    "Fetching..."
                                } else {
                                    viewModel.progress.value.toString() + " %"
                                }
                            )
                        }
                    )
                }
            }
        }
    }
}

