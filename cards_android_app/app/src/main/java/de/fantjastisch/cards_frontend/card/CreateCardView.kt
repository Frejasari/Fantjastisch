package de.fantjastisch.cards_frontend.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
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
import de.fantjastisch.cards_frontend.category.CategorySelect
import org.openapitools.client.models.ErrorEntryEntity
import java.util.*

//TODO Fehler anzeigen.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCardView(
    modifier: Modifier = Modifier
) {
    val viewModel = viewModel { CreateCardViewModel() }

    // Componente die ihre Kinder untereinander anzeigt.
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .fillMaxSize()
    ) {
        OutlinedTextField(
            maxLines = 1,
            keyboardActions = KeyboardActions(
                onDone = { viewModel.onAddCardClicked() },
            ),
            modifier = Modifier.fillMaxWidth(),
            value = viewModel.cardQuestion.value,
            supportingText = {
                val questionError = viewModel.errors.value.find { it.field == "question" }
                if (questionError != null) {
                    Text(text = mapError(questionError.code))
                }
            },
            onValueChange = { viewModel.cardQuestion.value = it },
            placeholder = { Text(text = stringResource(id = R.string.create_card_question_text)) },
        )
        OutlinedTextField(
            maxLines = 1,
            keyboardActions = KeyboardActions(
                onDone = { viewModel.onAddCardClicked() },
            ),
            modifier = Modifier.fillMaxWidth(),
            value = viewModel.cardAnswer.value,
            onValueChange = { viewModel.cardAnswer.value = it },
            placeholder = { Text(text = stringResource(id = R.string.create_card_answer_text)) },
        )
        OutlinedTextField(
            maxLines = 1,
            keyboardActions = KeyboardActions(
                onDone = { viewModel.onAddCardClicked() },
            ),
            modifier = Modifier.fillMaxWidth(),
            value = viewModel.cardTag.value,
            onValueChange = { viewModel.cardTag.value = it },
            placeholder = { Text(text = stringResource(id = R.string.create_card_tag_text)) },
        )
        CategorySelect(
            modifier = Modifier.weight(1f),
            categories = viewModel.cardCategories.value,
            onCategorySelected = viewModel::onCategorySelected
        )
        FilledTonalButton(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = viewModel::onAddCardClicked
        ) {
            Text(text = stringResource(R.string.create_card_save_button_text))
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


