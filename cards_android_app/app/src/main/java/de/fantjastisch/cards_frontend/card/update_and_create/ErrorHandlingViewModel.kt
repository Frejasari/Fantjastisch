package de.fantjastisch.cards_frontend.card.update_and_create

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import de.fantjastisch.cards_frontend.util.ErrorsEnum.*
import de.fantjastisch.cards_frontend.util.RepoResult.UnexpectedErrorType
import de.fantjastisch.cards_frontend.util.RepoResult.UnexpectedErrorType.UNEXPECTED_ERROR
import org.openapitools.client.models.ErrorEntryEntity

/**
 * Parent Class, die Errors enthält und Basis-Funktionalität bereitstellt. Bindeglied zu unserer Error-Snackbar
 *
 * @author Freja Sender
 */
abstract class ErrorHandlingViewModel : ViewModel() {

    val error = mutableStateOf(NO_ERROR)

    val errors = mutableStateOf<List<ErrorEntryEntity>>(emptyList())

    fun onToastShown() {
        error.value = NO_ERROR
    }

    fun setValidationErrors(errors: List<ErrorEntryEntity>) {
        error.value = CHECK_INPUT
        this.errors.value = errors
    }

    fun setUnexpectedError(cause: UnexpectedErrorType) {
        if (cause == UNEXPECTED_ERROR) {
            error.value = UNEXPECTED
        } else {
            error.value = NETWORK
        }
    }
}
