package de.fantjastisch.cards_frontend.learning_object_details

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.navigator.Navigator
import de.fantjastisch.cards.R
import de.fantjastisch.cards_frontend.infrastructure.FantMainNavigator
import de.fantjastisch.cards_frontend.learning_box.LearningBoxWitNrOfCards
import de.fantjastisch.cards_frontend.learning_mode.LearningModeFragment
import de.fantjastisch.cards_frontend.learning_object_details.LearningModeSortViewModel
import java.util.*


@Composable
fun LearningModeSortDialog(
    learningBox: LearningBoxWitNrOfCards,
    learningObjectId: UUID,
    isOpen: Boolean,
    setIsOpen: (isOpen: Boolean) -> Unit,
//    navigator: Navigator,
) {
    val navigator = FantMainNavigator.current
    val viewModel =
        viewModel(key = learningBox.id.toString()) {
            LearningModeSortViewModel()
        }
    val label =
        remember { mutableStateOf(if (viewModel.sort.value) "Alphabetisch" else "Zufällig") }


    if (isOpen) {
        AlertDialog(
            modifier = Modifier.padding(16.dp),
            onDismissRequest = { setIsOpen(false) },
            title = {
                Column() {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = stringResource(R.string.sort_cards_in_box_dialog_title),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = label.value, modifier = Modifier.weight(5f),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.padding(start = 8.dp))
                        Switch(
                            checked = viewModel.sort.value, onCheckedChange = {
                                viewModel.sort.value = !viewModel.sort.value
                                label.value =
                                    if (viewModel.sort.value) "Alphabetisch" else "Zufällig"
                            },
                            modifier = Modifier.weight(2f)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
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
                Button(
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
