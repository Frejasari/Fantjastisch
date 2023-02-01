package de.fantjastisch.cards_frontend.learning_object

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import de.fantjastisch.cards.R
import de.fantjastisch.cards_frontend.card.CardSelect
import de.fantjastisch.cards_frontend.category.CategorySelect
import de.fantjastisch.cards_frontend.components.SingleSelect
import de.fantjastisch.cards_frontend.infrastructure.CloseScreenOnSignalEffect
import java.util.*

//TODO Fehler anzeigen.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateLearningObjectView(
    modifier: Modifier = Modifier
) {
    val viewModel = viewModel { CreateLearningObjectViewModel() }

    var expanded by remember { mutableStateOf(false) }
    var expandedForCat by remember { mutableStateOf(false) }
    val rotate by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f
    )
    val rotateForCat by animateFloatAsState(
        targetValue = if (expandedForCat) 180f else 0f
    )


    Column(modifier = Modifier.fillMaxSize()) {
        // Componente die ihre Kinder untereinander anzeigt.
        LazyColumn(
            modifier = modifier
                .background(MaterialTheme.colorScheme.background)
                .weight(1f),
            contentPadding = PaddingValues(all = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Column() {
                    OutlinedTextField(
                        maxLines = 1,
                        keyboardActions = KeyboardActions(
                            onDone = { viewModel.onAddLearningObjectClicked() },
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        value = viewModel.learningObjectLabel.value,
                        onValueChange = viewModel::setLearningObjectLabel,
                        placeholder = { Text(text = stringResource(id = R.string.label_label)) },
                        label = { Text(text = stringResource(R.string.label_label)) },
                        isError = viewModel.learningObjectLabel.value.isBlank()
                    )
                    SingleSelect(
                        items = viewModel.learningSystems.value,
                        selectedItem = viewModel.selectedSystem.value,
                        onItemSelected = viewModel::onLearningSystemSelected,
                        placeholder = { Text(text = stringResource(R.string.learning_system_label)) }
                    )
                    Divider(Modifier.padding(horizontal = 20.dp, vertical = 20.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(id = R.string.add_categories_label),
                            modifier = Modifier.weight(4.25f),
                            style = MaterialTheme.typography.titleMedium
                        )
                        IconButton(
                            modifier = Modifier
                                .rotate(rotateForCat),
                            onClick = {
                                expandedForCat = !expandedForCat
                                if (expanded) {
                                    expanded = false
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "drop-down arrow"
                            )
                        }
                    }
                    if(expandedForCat) {
                        CategorySelect(
                            categories = viewModel.allCategories.value,
                            onCategorySelected = viewModel::onCategorySelected
                        )
                    }
                }


                Divider(Modifier.padding(horizontal = 20.dp, vertical = 20.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.select_cards_label),
                        modifier = Modifier.weight(4.25f),
                        style = MaterialTheme.typography.titleMedium
                    )
                    IconButton(
                        modifier = Modifier
                            .rotate(rotate),
                        onClick = {
                            expanded = !expanded
                            if (expandedForCat) {
                                expandedForCat = false
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "drop-down arrow"
                        )
                    }
                }
            }
            if(expanded) {
                CardSelect(
                    cards = viewModel.allCards.value,
                    onCardSelected = viewModel::onCardSelected
                )
            }

        }
        FilledTonalButton(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = viewModel::onAddLearningObjectClicked
        ) {
            Text(text = stringResource(R.string.save_button_text))
        }
    }

    CloseScreenOnSignalEffect(viewModel.isFinished.value)

    //C:\Users\Tamari Bayer\.android\avd\Pixel_6_Pro_API_29.avd
}