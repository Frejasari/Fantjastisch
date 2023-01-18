package de.fantjastisch.cards_frontend.category

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
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import de.fantjastisch.cards.R
import de.fantjastisch.cards_frontend.components.OutlinedTextFieldWithErrors
import org.openapitools.client.models.ErrorEntryEntity
import java.util.*

//TODO Fehler anzeigen.
@OptIn(ExperimentalMaterial3Api::class)
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
            value = viewModel.catLabel.value,
            onValueChange = { viewModel.catLabel.value = it },
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

fun mapError(code: ErrorEntryEntity.Code): String {
    return when (code) {
        ErrorEntryEntity.Code.cONSTRAINTVIOLATION -> "Darf nicht leer sein"
        ErrorEntryEntity.Code.nOCATEGORIESVIOLATION -> "Darf nicht leer sein"
        ErrorEntryEntity.Code.nOTNULLVIOLATION -> "Darf nicht leer sein"
        ErrorEntryEntity.Code.nOTBLANKVIOLATION -> "Darf nicht blank sein"
        ErrorEntryEntity.Code.lABELTAKENVIOLATION -> "Label schon vergeben"
        ErrorEntryEntity.Code.cATEGORYDOESNTEXISTVIOLATION -> "Categorie existiert nicht"
        ErrorEntryEntity.Code.sUBCATEGORYDOESNTEXISTVIOLATION -> "Categorie existiert nicht"
        ErrorEntryEntity.Code.cATEGORYNOTEMPTYVIOLATION -> "Es muss eine Kategorie ausgewaehlt werden"
        ErrorEntryEntity.Code.cYCLICSUBCATEGORYRELATIONVIOLATION -> "Zyklen sind nicht erlaubt"
        ErrorEntryEntity.Code.sUBCATEGORYISNULLVIOLATION -> "Subkategorien dürfen nicht null sein"
        ErrorEntryEntity.Code.eNTITYDOESNOTEXIST -> "Entität exisitert nicht"
        ErrorEntryEntity.Code.cARDDUPLICATEVIOLATION -> "Karte existiert schon."
    }
}

