package de.fantjastisch.cards_frontend.learning_object_details.sort_dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import de.fantjastisch.cards.R
import de.fantjastisch.cards_frontend.infrastructure.FantMainNavigator
import de.fantjastisch.cards_frontend.learning_box.LearningBoxWitNrOfCards
import de.fantjastisch.cards_frontend.learning_mode.LearningModeFragment
import java.util.*


@Composable
fun LearningModeSortDialog(
    learningBox: LearningBoxWitNrOfCards,
    learningObjectId: UUID,
    isOpen: Boolean,
    setIsOpen: (isOpen: Boolean) -> Unit,
) {
    val navigator = FantMainNavigator.current
    val viewModel = viewModel(key = learningBox.id.toString()) { LearningModeSortViewModel() }

    if (isOpen) {
        AlertDialog(
//            modifier = Modifier.padding(16.dp),
            onDismissRequest = { setIsOpen(false) },
            title = {
                Text(
                    text = stringResource(R.string.sort_cards_in_box_dialog_title),
                )
            },
            text = {
                Column(
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        RadioButton(
                            selected = viewModel.sort.value,
                            onClick = {
                                viewModel.onSortSelected(isAlphabetic = true)
                            })
                        Text(
                            text = stringResource(id = R.string.alphabetic),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        RadioButton(selected = !viewModel.sort.value,
                            onClick = {
                                viewModel.onSortSelected(
                                    isAlphabetic = false
                                )
                            })
                        Text(
                            text = stringResource(id = R.string.random),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        setIsOpen(false)
                        navigator.push(
                            LearningModeFragment(
                                learningBoxId = learningBox.id,
                                learningObjectId = learningObjectId,
                                sort = viewModel.sort.value
                            )
                        )
                    }) {
                    Text(text = stringResource(R.string.confirm))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        viewModel.onDismissClicked()
                    }) {
                    Text(text = stringResource(R.string.cancel))
                }
            }
        )
    }
    // einmaliger Effekt
    LaunchedEffect(
        // wenn sich diese Variable ändert
        key1 = viewModel.isFinished.value,
        // dann wird dieses Lambda ausgeführt.
        block = {
            if (viewModel.isFinished.value) {
                setIsOpen(false)
                viewModel.isFinished.value = false
            }
        })
}
