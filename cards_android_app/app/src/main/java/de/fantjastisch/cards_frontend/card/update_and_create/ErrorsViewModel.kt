package de.fantjastisch.cards_frontend.card.update_and_create

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import de.fantjastisch.cards_frontend.infrastructure.ErrorsEnum
import org.openapitools.client.models.ErrorEntryEntity

/**
 * Parent Class, die gemeinsame Daten  & Funktionen zur Fehlerbehandlung bereitstellt
 *
 * @author Freja Sender
 */
open class ErrorsViewModel : ViewModel() {

    val error = mutableStateOf(ErrorsEnum.NO_ERROR)
    val errors = mutableStateOf<List<ErrorEntryEntity>>(emptyList())

}
