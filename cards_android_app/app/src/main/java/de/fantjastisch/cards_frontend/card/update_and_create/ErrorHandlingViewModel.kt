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
open class ErrorHandlingViewModel : ViewModel() {

    val error = mutableStateOf(ErrorsEnum.NO_ERROR)

    val errors = mutableStateOf<List<ErrorEntryEntity>>(emptyList())

    fun onToastShown() {
        error.value = ErrorsEnum.NO_ERROR
    }
}
