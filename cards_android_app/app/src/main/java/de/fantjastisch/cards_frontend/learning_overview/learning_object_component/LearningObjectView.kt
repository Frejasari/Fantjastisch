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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import de.fantjastisch.cards.R
import de.fantjastisch.cards_frontend.infrastructure.FantMainNavigator
import de.fantjastisch.cards_frontend.infrastructure.effects.ShowErrorOnSignalEffect
import de.fantjastisch.cards_frontend.learning_object.LearningObject
import de.fantjastisch.cards_frontend.learning_object_details.LearningDetailsFragment
import de.fantjastisch.cards_frontend.learning_overview.delete.DeleteLearningObjectDialog
import de.fantjastisch.cards_frontend.util.LoadingWrapper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearningObjectView(
    learningObject: LearningObject,
    onDeleteSuccessful: () -> Unit
) {
    val navigator = FantMainNavigator.current
    val viewModel = viewModel(key = learningObject.id.toString()) {
        LearningObjectComponentViewModel(
            learningSystemId = learningObject.learningSystemId,
            learningObjectId = learningObject.id
        )
    }
    ShowErrorOnSignalEffect(viewModel = viewModel)

    val isDeleteDialogOpen = remember { mutableStateOf(false) }
    // Ein RecyclerView -> Eine lange liste von Eintraegen
    LoadingWrapper(isLoading = viewModel.isLoading.value)
    {
        Box(
            Modifier
                .clickable(
                    onClick = {
                        navigator.push(LearningDetailsFragment(learningObject.id))
                    })
        ) {
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
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = viewModel.learningSystemLabel.value
                        )
                        val isInverted = remember { mutableStateOf(false) }
                        SuggestionChip(
                            modifier = Modifier,
                            onClick = { isInverted.value = !isInverted.value },
                            label = {
                                Text(
                                    modifier = Modifier,
                                    color = if (isInverted.value) Color.White else viewModel.getColor(),
                                    text = if (viewModel.progress.value == -1) {
                                        stringResource(R.string.fetching_data_text)
                                    } else {
                                        String.format("%s %%", viewModel.progress.value.toString())
                                    }
                                )
                            },
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                containerColor = if (isInverted.value) viewModel.getColor() else Color.White
                            )
                        )
                    }
                }
            }
        }
    }
}

