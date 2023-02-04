package de.fantjastisch.cards_frontend.glossary.filter

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import de.fantjastisch.cards.R
import de.fantjastisch.cards_frontend.category.CategorySelect
import de.fantjastisch.cards_frontend.components.ExpandableRow
import de.fantjastisch.cards_frontend.infrastructure.effects.ShowErrorOnSignalEffect

/**
 * Rendert die View zum Filtern im Glossar.
 *
 * @property bottomSheetNavigator BottomScreen für die Filteransicht.
 *
 * @author Freja Sender, Semjon Nirmann
 */
@OptIn(ExperimentalMaterial3Api::class)
class GlossaryFilterView(val bottomSheetNavigator: BottomSheetNavigator) : AndroidScreen() {
    // remember -> state nicht neu erzeugen, wenn Funktion neu aufgerufen wird.

    /**
     * Der Inhalt der View zum Filtern der Karten
     *
     */
    @Composable
    override fun Content() {
        val viewModel = viewModel { GlossaryFilterViewModel() }
        ShowErrorOnSignalEffect(viewModel = viewModel)
        // Effekt, der die filter view wieder ausblendet, sobald daten erfolgreich geladen wurden.
        LaunchedEffect(
            // wenn sich diese Variable ändert
            key1 = viewModel.isFinished.value,
            // dann wird dieses Lambda ausgeführt.
            block = {
                if (viewModel.isFinished.value) {
                    bottomSheetNavigator.hide()
                }
            })

        var expanded by remember { mutableStateOf(true) }

        Column(
            modifier = Modifier
                .padding(all = 16.dp),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .weight(1f)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.sort_label),
                        fontWeight = FontWeight.Medium,
                    )
                    Switch(
                        checked = viewModel.sort.value,
                        onCheckedChange = viewModel::onSortClicked
                    )
                }
                Divider()
                Text(
                    text = stringResource(R.string.filter_label),
                    fontWeight = FontWeight.Medium,
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    value = viewModel.search.value,
                    onValueChange = viewModel::onSearchInput,
                    placeholder = { Text(text = stringResource(R.string.search_card_placeholder)) },
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    value = viewModel.tag.value,
                    onValueChange = viewModel::onTagInput,
                    placeholder = { Text(text = stringResource(R.string.search_tag_placeholder)) },
                )

                ExpandableRow(
                    expanded = expanded,
                    onClick = {
                        expanded = !expanded
                    },
                    headline = stringResource(id = R.string.select_category_label),
                ) {
                    CategorySelect(
                        categories = viewModel.categories.value,
                        onCategorySelected = viewModel::onCategorySelected
                    )
                }
            }

            ElevatedButton(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth(),
                onClick = viewModel::onConfirmClicked
            ) {
                Text(text = stringResource(R.string.save_button_text))
            }
        }

    }
}