package de.fantjastisch.cards_frontend.category.update_and_create

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import de.fantjastisch.cards.R
import de.fantjastisch.cards_frontend.category.CategorySelect
import de.fantjastisch.cards_frontend.category.CategoryViewModel
import de.fantjastisch.cards_frontend.components.OutlinedTextFieldWithErrors
import org.openapitools.client.models.ErrorEntryEntity

//TODO Fehler anzeigen.
@Composable
fun UpdateOrCreateCategoryView(
    modifier: Modifier = Modifier,
    viewModel : CategoryViewModel
) {

    // Componente die ihre Kinder untereinander anzeigt.
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .fillMaxSize()
    ) {
        OutlinedTextFieldWithErrors(
            maxLines = 1,
            value = viewModel.categoryLabel.value,
            onValueChange = { viewModel.categoryLabel.value = it },
            placeholder = stringResource(id = R.string.create_category_label_text),
            errors = viewModel.errors.value,
            field = "label"
        )

        viewModel.subcategories.value?.let {
            CategorySelect(
                modifier = Modifier.weight(1f),
                categories = it,
                onCategorySelected = viewModel::onCategorySelected
            )
        }

        FilledTonalButton(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = viewModel::save
        ) {
            Text(text = stringResource(R.string.create_category_save_button_text))
        }
    }

    val navigator = LocalNavigator.currentOrThrow
    // einmaliger Effekt
    LaunchedEffect(
        // wenn sich diese Variable ändert
        key1 = viewModel.isFinished.value,
        // dann wird dieses Lambda ausgeführt.
        block = {
            if (viewModel.isFinished.value) {
                navigator.pop()
            }
        })

}


