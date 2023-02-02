package de.fantjastisch.cards_frontend.learning_object.create

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import de.fantjastisch.cards.R
import de.fantjastisch.cards_frontend.card.CardSelect
import de.fantjastisch.cards_frontend.category.CategorySelect
import de.fantjastisch.cards_frontend.components.ExpandableRow
import de.fantjastisch.cards_frontend.components.OutlinedTextFieldWithErrors
import de.fantjastisch.cards_frontend.components.SingleSelect
import de.fantjastisch.cards_frontend.infrastructure.CloseScreenOnSignalEffect

//TODO Fehler anzeigen.
@Composable
fun CreateLearningObjectView(
    modifier: Modifier = Modifier
) {
    val viewModel = viewModel { CreateLearningObjectViewModel() }

    var expanded by remember { mutableStateOf(true) }
    var expandedForCards by remember { mutableStateOf(false) }
    var expandedForCategories by remember { mutableStateOf(false) }


    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
//            contentPadding = PaddingValues(all = 16.dp),
    ) {
        // Componente die ihre Kinder untereinander anzeigt.
        Column(modifier = Modifier.weight(1f)) {


            ExpandableRow(
                expanded = expanded,
                onClick = {
                    expanded = !expanded
                    expandedForCards = false
                    expandedForCategories = false
                },
                headline = stringResource(id = R.string.allgemein_learningobject_label),
            ) {
                OutlinedTextFieldWithErrors(
                    maxLines = 1,
                    value = viewModel.learningObjectLabel.value,
                    onValueChange = viewModel::setLearningObjectLabel,
                    placeholder = stringResource(id = R.string.label_label),
                    errors = viewModel.errors.value,
                    field = "label"
                )
                SingleSelect(
                    items = viewModel.learningSystems.value,
                    selectedItem = viewModel.selectedSystem.value,
                    onItemSelected = viewModel::onLearningSystemSelected,
                    placeholder = stringResource(R.string.learning_system_label),
                    field = "learningsystem",
                    errors = viewModel.errors.value
                )
            }
            Divider()

            ExpandableRow(
                expanded = expandedForCategories,
                onClick = {
                    expandedForCategories = !expandedForCategories
                    expandedForCards = false
                    expanded = false
                },
                headline = stringResource(id = R.string.add_categories_label),
            ) {
                CategorySelect(
                    categories = viewModel.allCategories.value,
                    onCategorySelected = viewModel::onCategorySelected
                )
            }

            Divider()

            ExpandableRow(
                expanded = expandedForCards,
                onClick = {
                    expandedForCards = !expandedForCards
                    expandedForCategories = false
                    expanded = false
                },
                headline = stringResource(id = R.string.select_cards_label),
            ) {
                LazyColumn(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    CardSelect(
                        cards = viewModel.allCards.value,
                        onCardSelected = viewModel::onCardSelected
                    )
                }
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

}