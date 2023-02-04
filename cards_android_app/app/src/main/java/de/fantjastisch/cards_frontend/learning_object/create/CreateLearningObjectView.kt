package de.fantjastisch.cards_frontend.learning_object.create

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import de.fantjastisch.cards.R
import de.fantjastisch.cards_frontend.card.CardSelect
import de.fantjastisch.cards_frontend.category.CategorySelect
import de.fantjastisch.cards_frontend.components.ExpandableRow
import de.fantjastisch.cards_frontend.components.OutlinedTextFieldWithErrors
import de.fantjastisch.cards_frontend.components.SaveLayout
import de.fantjastisch.cards_frontend.components.SingleSelect
import de.fantjastisch.cards_frontend.infrastructure.effects.CloseScreenOnSignalEffect
import de.fantjastisch.cards_frontend.infrastructure.effects.ShowErrorOnSignalEffect

/**
 * Zeigt die Seite zum Erstellen eines Lernobjektes an
 *
 * @param modifier Modifier für die Seite.
 *
 * @author Semjon Nirmann, Jessica Repty, Freja Sender
 */
@Composable
fun CreateLearningObjectView(
    modifier: Modifier = Modifier
) {
    val viewModel = viewModel { CreateLearningObjectViewModel() }
    CloseScreenOnSignalEffect(viewModel.isFinished.value)
    ShowErrorOnSignalEffect(viewModel = viewModel)

    var expanded by remember { mutableStateOf(true) }
    var expandedForCards by remember { mutableStateOf(false) }
    var expandedForCategories by remember { mutableStateOf(false) }
    SaveLayout(
        onSaveClicked = viewModel::onAddLearningObjectClicked,
        modifier = modifier
    ) {
        ExpandableRow(
            expanded = expanded,
            onClick = {
                expanded = !expanded
                expandedForCards = false
                expandedForCategories = false
            },
            headline = stringResource(id = R.string.allgemein_learningobject_label),
        ) {
            GeneralCreateLearningObjectFieldsView()
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


}

/**
 * Rendert die Eingabefelder für die Erstellung eines Lernobjekts.
 *
 * @author Semjon Nirmann, Jessica Repty, Freja Sender
 */
@Composable
private fun GeneralCreateLearningObjectFieldsView() {
    val viewModel = viewModel { CreateLearningObjectViewModel() }
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