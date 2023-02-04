package de.fantjastisch.cards_frontend.card.update_and_create

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import de.fantjastisch.cards_frontend.util.ErrorsEnum
import org.openapitools.client.models.ErrorEntryEntity

/**
 * Parent Class, die Errors enthält und Basis-Funkt
 *
 * @author Freja Sender
 */
abstract class ErrorHandlingViewModel : ViewModel() {

    val error = mutableStateOf(ErrorsEnum.NO_ERROR)

    val errors = mutableStateOf<List<ErrorEntryEntity>>(emptyList())

    fun onToastShown() {
        error.value = ErrorsEnum.NO_ERROR
    }

    protected fun setValidationErrors(errors: List<ErrorEntryEntity>) {
        error.value = ErrorsEnum.CHECK_INPUT
        this.errors.value = errors
    }

    protected fun setUnexpectedErrors() {
        error.value = ErrorsEnum.UNEXPECTED
    }
}
