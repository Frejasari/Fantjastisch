package de.fantjastisch.cards_frontend.card.update

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import de.fantjastisch.cards_frontend.card.update_and_create.CardEdit
import org.openapitools.client.models.ErrorEntryEntity
import java.util.*


//TODO Fehler anzeigen.
@Composable
fun UpdateCardView(
    modifier: Modifier = Modifier,
    id: UUID
) {

    val viewModel = viewModel { UpdateCardViewModel(id = id) }
    // Componente die ihre Kinder untereinander anzeigt.
    CardEdit(
        modifier = modifier,
        question = TextFieldState(
            value = viewModel.cardQuestion.value,
            errors = viewModel.errors.value,
            onValueChange = viewModel::setCardQuestion,
        ),
        answer = TextFieldState(
            value = viewModel.cardAnswer.value,
            errors = viewModel.errors.value,
            onValueChange = viewModel::setCardAnswer,
        ),
        tag = TextFieldState(
            value = viewModel.cardTag.value,
            errors = viewModel.errors.value,
            onValueChange = viewModel::setCardTag,
        ),
        categories = viewModel.cardCategories.value,
        onCategorySelected = viewModel::onCategorySelected,
        onUpdateCardClicked = viewModel::onUpdateCardClicked,
    )

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

data class TextFieldState(
    val value: String,
    val errors: List<ErrorEntryEntity>,
    val onValueChange: (String) -> Unit,
)


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
        ErrorEntryEntity.Code.bOXLABELSISNULLVIOLATION -> "Die Box Labels dürfen nicht leer sein."
    }
}


