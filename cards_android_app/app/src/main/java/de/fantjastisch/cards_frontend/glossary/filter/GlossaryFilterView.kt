package de.fantjastisch.cards_frontend.glossary.filter

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import de.fantjastisch.cards.R
import de.fantjastisch.cards_frontend.category.CategorySelect
import de.fantjastisch.cards_frontend.components.SaveLayout

@OptIn(ExperimentalMaterial3Api::class)
class GlossaryFilterView(val bottomSheetNavigator: BottomSheetNavigator) : AndroidScreen() {
    // remember -> state nicht neu erzeugen, wenn Funktion neu aufgerufen wird.

    @Composable
    override fun Content() {
        val viewModel = viewModel { GlossaryFilterViewModel() }
        SaveLayout(
            onSaveClicked = viewModel::onLoadPageClicked,
            modifier = Modifier
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
                Switch(checked = viewModel.sort.value, onCheckedChange = viewModel::onSortClicked)
            }
            Divider()
            Text(
                text = stringResource(R.string.filter_label),
                fontWeight = FontWeight.Medium,
            )
            OutlinedTextField(
                maxLines = 2,
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.search.value,
                onValueChange = viewModel::onSearchInput,
                placeholder = { Text(text = stringResource(R.string.search_card_placeholder)) },
            )
            OutlinedTextField(
                maxLines = 2,
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.tag.value,
                onValueChange = viewModel::onTagInput,
                placeholder = { Text(text = stringResource(R.string.search_tag_placeholder)) },
            )
            Text(
                text = stringResource(R.string.select_category_label),
                fontWeight = FontWeight.Medium,
            )
            CategorySelect(
                categories = viewModel.categories.value,
                onCategorySelected = viewModel::onCategorySelected
            )
        }

        LaunchedEffect(
            // wenn sich diese Variable ändert
            key1 = viewModel.isFinished.value,
            // dann wird dieses Lambda ausgeführt.
            block = {
                if (viewModel.isFinished.value) {
                    bottomSheetNavigator.hide()
                }
            })
    }
}