package de.fantjastisch.cards_frontend.glossary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import de.fantjastisch.cards.R
import de.fantjastisch.cards_frontend.category.CategorySelect
import de.fantjastisch.cards_frontend.glossary.GlossaryFilterViewModel

@OptIn(ExperimentalMaterial3Api::class)
class GlossaryFilterView(val bottomSheetNavigator: BottomSheetNavigator) : AndroidScreen() {
    // remember -> state nicht neu erzeugen, wenn Funktion neu aufgerufen wird.

    @Composable
    override fun Content() {
        val viewModel = viewModel { GlossaryFilterViewModel() }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = "Filter",
                fontWeight = FontWeight(500),
                fontSize = 20.sp
            )
            Divider()
            OutlinedTextField(
                maxLines = 2,
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.search.value,
                onValueChange = viewModel::onSearchInput,
                placeholder = { Text(text = "Nach Karten suchen") },
            )
            OutlinedTextField(
                maxLines = 2,
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.tag.value,
                onValueChange = viewModel::onTagInput,
                placeholder = { Text(text = "Nach Tag suchen") },
            )
            Divider()
            Text(
                text = "Kategorien auswählen",
                style = MaterialTheme.typography.titleMedium
            )
            CategorySelect(
                categories = viewModel.categories.value,
                onCategorySelected = viewModel::onCategorySelected
            )
            FilledTonalButton(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = viewModel::onLoadPageClicked
            ) {
                Text(text = stringResource(R.string.create_card_save_button_text))
            }

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