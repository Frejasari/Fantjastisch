package de.fantjastisch.cards_frontend.card.update

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import de.fantjastisch.cards_frontend.card.delete.DeleteCardDialog
import de.fantjastisch.cards_frontend.card.update_and_create.CardEdit
import de.fantjastisch.cards_frontend.glossary.GlossaryViewModel
import de.fantjastisch.cards_frontend.infrastructure.CloseScreenOnSignalEffect
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
  /*  CardEdit(
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
        linkName = TextFieldState(
            value = viewModel.linkName.value,
            errors = viewModel.errors.value,
            onValueChange = viewModel::setLinkName,
        ),
        cards = viewModel.cards.value,
        onCardSelected = viewModel::onCardSelected,
        onCreateLinkClicked = viewModel::onCreateLinkClicked,
        links = viewModel.cardLinks.value,
        onDeleteLinkClicked = viewModel::onDeleteLinkClicked

    ) */

    CloseScreenOnSignalEffect(shouldClose = viewModel.isFinished.value)
}

data class TextFieldState(
    val value: String,
    val errors: List<ErrorEntryEntity>,
    val onValueChange: (String) -> Unit,
)


fun mapError(code: ErrorEntryEntity.Code): String {
    return when (code) {
        ErrorEntryEntity.Code.cONSTRAINTVIOLATION -> "Feld darf nicht leer sein."
        ErrorEntryEntity.Code.nOCATEGORIESVIOLATION -> "Feld darf nicht leer sein."
        ErrorEntryEntity.Code.nOTNULLVIOLATION -> "Feld darf nicht leer sein."
        ErrorEntryEntity.Code.nOTBLANKVIOLATION -> "Feld darf nicht leer sein."
        ErrorEntryEntity.Code.lABELTAKENVIOLATION -> "Label ist bereits vergeben."
        ErrorEntryEntity.Code.cATEGORYDOESNTEXISTVIOLATION -> "Kategorie existiert nicht."
        ErrorEntryEntity.Code.sUBCATEGORYDOESNTEXISTVIOLATION -> "Kategorie existiert nicht."
        ErrorEntryEntity.Code.cATEGORYNOTEMPTYVIOLATION -> "Es muss eine Kategorie ausgewaehlt werden."
        ErrorEntryEntity.Code.cYCLICSUBCATEGORYRELATIONVIOLATION -> "Zyklen sind nicht erlaubt."
        ErrorEntryEntity.Code.sUBCATEGORYISNULLVIOLATION -> "Subkategorien dürfen nicht null sein."
        ErrorEntryEntity.Code.eNTITYDOESNOTEXIST -> "Entität exisitert nicht."
        ErrorEntryEntity.Code.cARDDUPLICATEVIOLATION -> "Karte existiert bereits."
        ErrorEntryEntity.Code.bOXLABELSISNULLVIOLATION -> "Die Box-Labels dürfen nicht leer sein."
    }
}


